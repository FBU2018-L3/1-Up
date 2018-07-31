package com.l3.one_up.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.l3.one_up.R;
import com.l3.one_up.adapters.FeedItemAdapter;
import com.l3.one_up.adapters.FriendsAdapter;
import com.l3.one_up.interfaces.FacebookCallComplete;
import com.l3.one_up.interfaces.PowerUpCallback;
import com.l3.one_up.model.Event;
import com.l3.one_up.model.FacebookQuery;
import com.l3.one_up.model.OrderFacebookUsersById;
import com.l3.one_up.model.OrderParseUsersByFbId;
import com.l3.one_up.model.PowerUp;
import com.l3.one_up.model.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment implements FacebookCallComplete {
    final static String tag = "FriendsFragment";
    /* our "context" */
    FragmentActivity fragAct;
    /* our data set */
    ArrayList<FacebookQuery.FacebookUser> friendsList;
    /* Array list to mirror facebook users on the parse side */
    ArrayList<User> parseUsers;
    /* our recycler view */
    private RecyclerView rvFriendList;
    /* our adapter */
    private FriendsAdapter friendsAdapter;
    /* our text view used for notifying user of any friend updates kinda */
    private TextView tvNoFriends;
    /* friend feed recycler view */
    private RecyclerView rvFriendFeed;
    /* data set for our friend feed */
    private ArrayList<Event> friendEvents;
    /* adapter for  our friend feed */
    private FeedItemAdapter feedItemAdapter;
    /* power up button */
    private Button btPowerUp;
    /* int to show us which user we are looking at at any given time */
    private int positionAtUser;
    /* flag to tell us whether our data set is complete */
    private boolean isComplete;
    /* array list of complete data so we don't continually check it */
    private ArrayList<FacebookQuery.FacebookUser> completeFriendsList;
    private ArrayList<User> completeParseUser;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance() {
        Bundle args = new Bundle();
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(tag, "In our friends fragment");
        /* set up our context */
        fragAct = (FragmentActivity) getActivity();
        isComplete = false;
        tvNoFriends = fragAct.findViewById(R.id.tvNofriends);
        /* hook up power up button */
        btPowerUp = fragAct.findViewById(R.id.btPowerUp);
        /* set up recycler view */
        rvFriendList = fragAct.findViewById(R.id.rvFriendList);
        /* Set up our layout Manager to be cool */
        GridLayoutManager coolLayout = new GridLayoutManager(fragAct, 1, GridLayoutManager.HORIZONTAL, false);
        rvFriendList.setLayoutManager(coolLayout);
        /* init data set */
        friendsList = new ArrayList<>();
        parseUsers = new ArrayList<>();
        completeFriendsList = new ArrayList<>();
        completeParseUser = new ArrayList<>();
        /* set as adapter */
        friendsAdapter = new FriendsAdapter(friendsList);
        rvFriendList.setAdapter(friendsAdapter);
        /* set up the the friend feed thingz */
        friendEvents = new ArrayList<>();
        feedItemAdapter = new FeedItemAdapter(friendEvents);
        rvFriendFeed = fragAct.findViewById(R.id.rvFriendFeed);
        rvFriendFeed.setLayoutManager(new LinearLayoutManager(fragAct));
        rvFriendFeed.setAdapter(feedItemAdapter);
        /* init to an impossible number */
        positionAtUser = -1;
        /* boolean flag that let us know whether data set is complete */
        setHasOptionsMenu(true);


        /* Time to do our query and then update the adapter in the callbacks */
        if(isLoggedIn()){
            tvNoFriends.setVisibility(TextView.GONE);
            FacebookQuery query = new FacebookQuery();
            query.getFriends(this);
            /* set on click listeners for power up */
            btPowerUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendPowerUp();
                }
            });
            /* init all the search bar stuff */
        }
        else{
            Log.d(tag, "User is not logged in! Please connect to facebook!");
            tvNoFriends.setVisibility(TextView.VISIBLE);
            tvNoFriends.setText("You are not connected to Facebook! Please connect to Facebook to use this feature!");
        }
    }


    /* NOTE: KEEP ALL DATA PROCESSING WITHIN THE CALLBACKS */
    @Override
    public void notifyCompleteList(final ArrayList<FacebookQuery.FacebookUser> FacebookList, ArrayList<String> friendIds) {
        /* Time to make some queries */
        if(FacebookList.size() == 0){
            Log.d(tag, "Logged user has no facebook friends. Break the news :(");
            tvNoFriends.setVisibility(TextView.VISIBLE);
            tvNoFriends.setText("None of your facebook friends on the app :( Invite them to the app to use this feature!");
            return;
        }

        User.Query userQuery = new User.Query();
        userQuery.returnWithFacebookIds(friendIds);
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if(e == null){
                    for(int i = 0; i < objects.size(); i++){
                        parseUsers.add(objects.get(i));
                    }
                    Log.d(tag, "Parse user array has size of: " + objects.size());
                    /* time to sort both data sets to match parse user and facebook user */
                    OrderFacebookUsersById fbCompare = new OrderFacebookUsersById();
                    OrderParseUsersByFbId parseCompare = new OrderParseUsersByFbId();
                    // sort!!!
                    FacebookList.sort(fbCompare);
                    parseUsers.sort(parseCompare);
                    /* store a final copy */
                    completeParseUser.addAll(parseUsers);
                    completeFriendsList.addAll(FacebookList);


                    if(parseUsers.size() == FacebookList.size()){
                        setLevels(FacebookList, parseUsers);
                        /* Note: might want to move all this stuff into the above conditional if I think it'll be a problem */
                        updateFBAdapterDataSet(FacebookList);
                        /* data set is complete set flag */
                        isComplete = true;
                        /* load in events for the first user in the data set */
                        loadFriendEvents(parseUsers.get(0));
                        positionAtUser = 0;
                        rvFriendList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if(newState == recyclerView.SCROLL_STATE_IDLE){
                                    int position = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                                    positionAtUser = position;
                                    if(position>=0) {
                                        User atUser = parseUsers.get(position);
                                        loadFriendEvents(atUser);
                                    }
                                }
                            }
                        });

                    } else {
                        Log.e(tag ,"UM NUMBER OF USERS DO NOT MATCH DO NOT TRY LINKING THE INFO TOGETHER");
                    }
                }
                else{
                    Toast.makeText(fragAct, "something went wrong fetching users :(", Toast.LENGTH_LONG);
                    e.printStackTrace();
                }
            }
        });
        return;
    }


    /* moving a lot of grunt work into functions for cleaner (for the the aesthetic really) code */
    public void setLevels(ArrayList<FacebookQuery.FacebookUser> FacebookList, ArrayList<User> ParseUsers)
    {
        for(int i = 0; i < ParseUsers.size(); i++){
            int parseUserLevel = ParseUsers.get(i).getLevel();
            String parseUsername = ParseUsers.get(i).getUsername();
            FacebookList.get(i).setUserLevel(parseUserLevel);
            FacebookList.get(i).setParseUsername(parseUsername);
        }
    }

    public void updateFBAdapterDataSet(ArrayList<FacebookQuery.FacebookUser> facebookList) {
        friendsList.clear();
        friendsList.addAll(facebookList);
        /* init a final complete data set which we should not amend */
        friendsAdapter.notifyDataSetChanged();
    }

    public boolean isLoggedIn(){
        if(AccessToken.getCurrentAccessToken() != null) return true;
        else return false;
    }

    public void loadFriendEvents(User user){
        /* do the query based on the position, use callback to ensure we are working with complete data */
        Event.Query eventQuery = new Event.Query();
        eventQuery.includeActivity().byUser(user).onlyThisWeek().mostRecentFirst().getPublicEvents();
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException e) {
                /* use call backs here to ensure that we are only working with complete data*/
                if(e == null){
                    friendEvents.clear();
                    for(int i = 0; i < objects.size(); i++){
                        friendEvents.add(objects.get(i));
                    }
                    feedItemAdapter.notifyDataSetChanged();
                }
                else {
                    Log.d(tag, "Failed to get events by user :(");
                }
            }
        });
    }

    /* take care of all the power up things */
    private void sendPowerUp() {
        /* condition to check whether we are a valid position */
        if(positionAtUser >= 0) {
            Toast.makeText(fragAct, "Valid position!", Toast.LENGTH_LONG).show();
            User atUser = parseUsers.get(positionAtUser);
            Log.d(tag, "At user: " + atUser.getFacebookId());
            PowerUp newPowerUp = new PowerUp();
            newPowerUp.setIsRedeemed(false);
            newPowerUp.setXP(10);
            /* set our pointers */
            User sentByUser = User.getCurrentUser();
            Log.d(tag, "Sent by: " + sentByUser.getUsername());
            User sentToUser = parseUsers.get(positionAtUser);
            Log.d(tag, "Sent to: " + sentToUser.getUsername());
            newPowerUp.setSentByUser(sentByUser);
            newPowerUp.setSentToUser(sentToUser);
            /* save time */
            FragmentManager fragmentManager = getFragmentManager();
            SendPowerUpFragment sendPowerUpFragment = SendPowerUpFragment.newInstance(newPowerUp);
            sendPowerUpFragment.show(fragmentManager, "tagz");

        }
        else{
            Toast.makeText(fragAct, "Please move to a valid position", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /* mplementing ActionBar Search inside a fragment */
        MenuItem item = menu.add("Search");
        item.setIcon(R.drawable.search_icon);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        android.support.v7.widget.SearchView searchView = new android.support.v7.widget.SearchView(getActivity());

        /* Fetch our text view and set appropriate fields */
        TextView textView = (AutoCompleteTextView) searchView.findViewById(R.id.search_src_text);
        textView.setHint("Search by Facebook Name ");
        textView.setHintTextColor(getResources().getColor(R.color.hintColor));
        textView.setTextColor(getResources().getColor(R.color.writeColor));
        /* implement the listener */
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /* once the user hits the submit bar */
                Log.d(tag, "User hit submit");
                doSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /* every time the user changes their search */
                if(TextUtils.isEmpty(newText)){
                    /* if the user has cleared the search, we want to show all activities */
                    doSearch("");
                    return false;
                }
                return false;
            }
        });
        item.setActionView(searchView);
    }

    private void doSearch(String query) {
        /* time to user our complete set to create a searched set */
        friendsList.clear();
        parseUsers.clear();

        if(query == ""){
            Log.d(tag, "Empty query so return all results");
            friendsList.addAll(completeFriendsList);
            parseUsers.addAll(completeParseUser);
        }
        else{
            query.toLowerCase();
            Log.d(tag, "Query is: " + query);
            for(int i = 0; i < completeFriendsList.size(); i++){
                String facebookUsername = completeFriendsList.get(i).username;
                facebookUsername.toLowerCase();
                /* if the facebook username contains the query, we add it back to our data */
                if(facebookUsername.contains(query)){
                    Log.d(tag, "Found one. Name: " + facebookUsername);
                    friendsList.add(completeFriendsList.get(i));
                    parseUsers.add(completeParseUser.get(i));
                }
            }
        }
        friendsAdapter.notifyDataSetChanged();
        if(parseUsers.size() != 0) loadFriendEvents(parseUsers.get(0));

    }
}
