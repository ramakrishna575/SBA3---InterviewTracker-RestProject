package com.wellsfargo.sba3.its.service;

import java.util.Set;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wellsfargo.sba3.its.dao.InterviewDAO;
import com.wellsfargo.sba3.its.dao.UserDAO;
import com.wellsfargo.sba3.its.entity.InterviewEntity;
import com.wellsfargo.sba3.its.entity.UserEntity;
import com.wellsfargo.sba3.its.exception.InterviewTrackerException;
import com.wellsfargo.sba3.its.model.AttendeeModel;
import com.wellsfargo.sba3.its.model.InterviewModel;
import com.wellsfargo.sba3.its.model.UserModel;

@Service
public class InterviewServiceImpl implements InterviewService {

	@Autowired
	private InterviewDAO intwRepo;
	
	@Autowired
	private UserDAO userRepo;

	private InterviewEntity toInterviewEntity(InterviewModel intwModel) {
		if(intwModel.getAttendee()==null)
			return new InterviewEntity(intwModel.getInterviewId(), intwModel.getInterviewerName(), intwModel.getInterviewName(), intwModel.getUsersSkills(), intwModel.getTime(), intwModel.getDate(), intwModel.getInterviewStatus(), intwModel.getRemarks());
		else
			return new InterviewEntity(intwModel.getInterviewId(), intwModel.getInterviewerName(), intwModel.getInterviewName(), intwModel.getUsersSkills(), intwModel.getTime(), intwModel.getDate(), intwModel.getInterviewStatus(), intwModel.getRemarks(),toUserEntities(intwModel.getAttendee()));
	}
	
	private InterviewModel toInterviewModel(InterviewEntity entity) {		
		if(entity.getAttendees()==null)
			return new InterviewModel(entity.getInterviewId(), entity.getInterviewerName(), entity.getInterviewName(), entity.getUsersSkills(), entity.getTime(), entity.getDate(), entity.getInterviewStatus(), entity.getRemarks());
		else
			return new InterviewModel(entity.getInterviewId(), entity.getInterviewerName(), entity.getInterviewName(), entity.getUsersSkills(), entity.getTime(), entity.getDate(), entity.getInterviewStatus(), entity.getRemarks(), toUserModels(entity.getAttendees()));
	}
	
	private Set<UserEntity> toUserEntities(Set<UserModel> userModels) {
		Set<UserEntity> entities=null;
		entities = userModels.stream().map(e -> toUserEntity(e)).collect(Collectors.toSet());
		return entities;
	}
	
	private UserEntity toUserEntity(UserModel model) {
		return new UserEntity(model.getUserId(), model.getFirstName(), model.getLastName(),model.getEmail(), model.getMobile());
	}
	
	private Set<UserModel> toUserModels(Set<UserEntity> userEntities) {
		Set<UserModel> models=null;
		models = userEntities.stream().map(e -> toUserModel(e)).collect(Collectors.toSet());
		return models;
	}
	
	private UserModel toUserModel(UserEntity entity) {
		return new UserModel(entity.getUserId(), entity.getFirstName(), entity.getLastName(),entity.getEmail(), entity.getMobile());
	}
	
	@Override
	@Transactional
	public InterviewModel add(InterviewModel interview) throws InterviewTrackerException {
        if(interview!=null) {
            if(intwRepo.existsById(interview.getInterviewId())) {
                throw new InterviewTrackerException("Interview Id already used!");
            }
        	interview = toInterviewModel(intwRepo.save(toInterviewEntity(interview)));
        }
        return interview;
	}
	
	@Override
	@Transactional
	public String addAttendee(AttendeeModel attendee) throws InterviewTrackerException {
		if(attendee!=null) {
			if(!userRepo.existsById(attendee.getUserId())) {
				throw new InterviewTrackerException("User Not Found");
			}
	        if(!intwRepo.existsById(attendee.getInterviewId())) {
	            throw new InterviewTrackerException("Interview Id Not Found!");
	        }
	        InterviewModel interview = getInterviewById(attendee.getInterviewId());
	        for(UserModel user: interview.getAttendee()) {
	        	if(user.getUserId() == attendee.getUserId()) {
	        		throw new InterviewTrackerException("User Id already exists on Interview!");
	        	}
	        }
	        Set<UserModel> users=interview.getAttendee();	        
	        users.add(getUserById(attendee.getUserId()));
	        interview.setAttendee(users);
	        intwRepo.save(toInterviewEntity(interview));
	        return "User with Id: " +  getUserById(attendee.getUserId()) + " added successfully to Interview";
		}
		return "Error adding User with Id: " +  attendee.getUserId();
	}

	public UserModel getUserById(int userId) {
	    UserEntity entity = userRepo.findById(userId).orElse(null);
	    return entity!=null?toUserModel(entity):null;
	}
	

	@Override
	public boolean deleteInterview(int interviewId) throws InterviewTrackerException {
		if(!intwRepo.existsById(interviewId)) {
			throw new InterviewTrackerException("Interview Id Not Found!");
		}		
		intwRepo.deleteById(interviewId);
		return false;
	}

	@Override
	public InterviewModel updateStatus(Integer interviewId, String status) throws InterviewTrackerException {
		if(!intwRepo.existsById(interviewId)) {
			throw new InterviewTrackerException("Interview Id Not Found!");
		}
		InterviewModel interview = getInterviewById(interviewId);
		interview.setInterviewStatus(status);
        intwRepo.save(toInterviewEntity(interview));
		return getInterviewModel(toInterviewEntity(interview));
	}

	@Override
	public Set<InterviewModel> getinterview(String interviewName, String interviewerName) {
		Set<InterviewEntity> entities= (Set<InterviewEntity>) intwRepo.findByNameAndInterviewer(interviewName, interviewerName);
		Set<InterviewModel> models=null;
		if(entities!=null && !entities.isEmpty()) {
			models = entities.stream().map(e -> getInterviewModel(e)).collect(Collectors.toSet());
		}
		return models;
	}

	@Override
	public String getInterviewCount() {
		Set<InterviewEntity> entities= (Set<InterviewEntity>) intwRepo.findAll();
		if(entities!=null)
			return "Total no. of Interviews: " + entities.size();
		return "No interviews are present";
	}

	@Override
	public Set<InterviewModel> getAllInterviewDetails() {
		Set<InterviewEntity> entities= (Set<InterviewEntity>) intwRepo.findAll();
		Set<InterviewModel> models=null;
		if(entities!=null && !entities.isEmpty()) {
			models = entities.stream().map(e -> getInterviewModel(e)).collect(Collectors.toSet());
		}
		return models;
	}
	
	private InterviewModel getInterviewModel(InterviewEntity entity) {
		return new InterviewModel(entity.getInterviewId(), entity.getInterviewerName(), entity.getInterviewName(), entity.getUsersSkills(), entity.getTime(), entity.getDate(), entity.getInterviewStatus(), entity.getRemarks());
	}
	@Override
	public Set<UserModel> showUsers(int interviewId) throws InterviewTrackerException {
		if(!intwRepo.existsById(interviewId)) {
            throw new InterviewTrackerException("Interview Id Not Found!");
        }
		return toUserModels(intwRepo.findById(interviewId).orElse(null).getAttendees());
	}

	@Override
	public InterviewModel getInterviewById(int iId) {
        InterviewEntity entity = intwRepo.findById(iId).orElse(null);
        return entity!=null?toInterviewModel(entity):null;
    }
}
