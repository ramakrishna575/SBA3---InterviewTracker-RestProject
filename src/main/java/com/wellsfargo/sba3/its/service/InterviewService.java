package com.wellsfargo.sba3.its.service;

import java.util.List;

import com.wellsfargo.sba3.its.exception.InterviewTrackerException;
import com.wellsfargo.sba3.its.model.AttendeeModel;
import com.wellsfargo.sba3.its.model.InterviewModel;
import com.wellsfargo.sba3.its.model.UserModel;

public interface InterviewService {

	InterviewModel add(InterviewModel interview) throws InterviewTrackerException;
	
	boolean deleteInterview(int interviewId) throws InterviewTrackerException;
	
	InterviewModel updateStatus(Integer interviewid, String status) throws InterviewTrackerException;
	
	List<InterviewModel> getinterview(String interviewName, String interviewerName);
	
	String getInterviewCount();
	
	List<InterviewModel> getAllInterviewDetails();
	
	List<UserModel> showUsers(int interviewId) throws InterviewTrackerException;

	String addAttendee(AttendeeModel attendee) throws InterviewTrackerException;

	InterviewModel getInterviewById(int interviewId);;
}
