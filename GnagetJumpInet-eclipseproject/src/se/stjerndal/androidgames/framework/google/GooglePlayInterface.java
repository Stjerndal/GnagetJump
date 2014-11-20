package se.stjerndal.androidgames.framework.google;

public interface GooglePlayInterface {
	
	public void Login();
	public void LogOut();

	//Find out whether the client is signed in to Google+
	public boolean getSignedIn();

	//Submit a score to a leaderboard
	public void submitScore(int score);

	//Fetch the scores and display them through Googles default widget
	public void getScores();

	//Fetch the score and gives access to the raw score data
	public void getScoresData();
	}