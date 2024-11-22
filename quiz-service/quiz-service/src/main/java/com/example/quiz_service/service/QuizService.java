package com.example.quiz_service.service;


import com.example.quiz_service.Dao.QuizDao;
import com.example.quiz_service.feign.QuizInterface;
import com.example.quiz_service.model.QuestionWrapper;
import com.example.quiz_service.model.Quiz;
import com.example.quiz_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    QuizDao quizdao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

     List<Integer> questions = quizInterface.getQuestionsForQuiz(category,numQ).getBody();
     //calling the generate url in question-service
        Quiz quiz = new Quiz();
        quiz.setQuestionIds(questions);
        quiz.setTitle(title);
        quizdao.save(quiz);


        return new ResponseEntity<>("Success", HttpStatus.CREATED);

    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {

        Quiz quiz = quizdao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionIds);
        return questions;
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }
}
