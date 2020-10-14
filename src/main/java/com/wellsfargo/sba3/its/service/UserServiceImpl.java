package com.wellsfargo.sba3.its.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wellsfargo.sba3.its.dao.InterviewDAO;
import com.wellsfargo.sba3.its.dao.UserDAO;
import com.wellsfargo.sba3.its.entity.InterviewEntity;
import com.wellsfargo.sba3.its.entity.UserEntity;
import com.wellsfargo.sba3.its.exception.InterviewTrackerException;
import com.wellsfargo.sba3.its.model.UserModel;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userRepo;

	@Autowired
	private InterviewDAO intwRepo;
	
	private UserEntity toEntity(UserModel model) {
		return new UserEntity(model.getUserId(), model.getFirstName(), model.getLastName(),model.getEmail(), model.getMobile());
	}
	
	private UserModel toModel(UserEntity entity) {
		return new UserModel(entity.getUserId(),entity.getFirstName(), entity.getLastName(), entity.getEmail(), entity.getMobile());
	}

	
	@Override
	@Transactional
	public UserModel add(UserModel user) throws InterviewTrackerException {
        if(user!=null) {
            if(userRepo.existsById(user.getUserId())) {
                throw new InterviewTrackerException("User Id already used!");
            }
            
            user = toModel(userRepo.save(toEntity(user)));
        }
        return user;
	}

	@Override
	@Transactional
	public boolean deleteUser(int userId) throws InterviewTrackerException {
		if(!userRepo.existsById(userId)) {
			throw new InterviewTrackerException("User Not Found");
		}
		UserEntity userEntity = userRepo.findById(userId).orElse(null);
		//userEntity.getInterviews().removeAll(userEntity.getInterviews());

		//userRepo.save(userEntity);
		userEntity.removeInterviews();
		userRepo.flush();
		userRepo.delete(userEntity);
		return true;
	}

	@Override
	public UserModel getUser(int userId) {
        UserEntity entity = userRepo.findById(userId).orElse(null);
        return entity!=null?toModel(entity):null;
	}

	@Override
	public List<UserModel> getAllUsers() {
		List<UserEntity> entities= userRepo.findAll();
		List<UserModel> models=null;
		if(entities!=null && !entities.isEmpty()) {
			models = entities.stream().map(e -> toModel(e)).collect(Collectors.toList());
		}
		return models;
	}

}
