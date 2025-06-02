package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.jpaentity.Feedback;
import com.smartenglishbackend.jparepo.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FeedBackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    public void addFeedback(Feedback feedback, Integer userId) {
        feedback.setAccountId(userId);
        feedback.setDate(LocalDate.now());
        feedbackRepository.save(feedback);
    }
    public void removeFeedback(Integer feedbackId) {
        Optional<Feedback> feedback = feedbackRepository.findById(feedbackId);
        if(feedback.isEmpty()){
            throw new MyResourceNotFoundException("feedback not found");
        }
        feedbackRepository.deleteById(feedbackId);
    }
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
}
