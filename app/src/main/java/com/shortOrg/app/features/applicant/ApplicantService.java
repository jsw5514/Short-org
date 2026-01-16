package com.shortOrg.app.features.applicant;

import com.shortOrg.app.domain.Applicant;
import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.repository.ApplicantRepository;
import com.shortOrg.app.repository.PostRepository;
import com.shortOrg.app.repository.UserRepository;
import com.shortOrg.app.shared.dto.ApplicantDto;
import com.shortOrg.app.shared.dto.ApplicantStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    // 모임 신청
    public void applicantRequest(Long postId, String userId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

//        if(applicantRepository.existsByPostIdAndUserId(post, user))
//            throw new RuntimeException("이미 신청 완료한 게시글");

        Applicant applicant = new Applicant();

        applicant.setId(null);
        applicant.setUserId(user);
        applicant.setPostId(post);
        applicant.setState(ApplicantStatus.PENDING);

        applicantRepository.save(applicant);
    }

    public List<ApplicantDto> applicantShow(Post postId) {
        // entity -> dto
        List<Applicant> aList = applicantRepository.findByPostId(postId);
        List<ApplicantDto> dtos = new ArrayList<>();

        for (Applicant applicant : aList) {
            dtos.add(convertToDto(applicant));
        }

        return dtos;
    }

    private ApplicantDto convertToDto(Applicant applicant){
        ApplicantDto applicantDto = new ApplicantDto();

        applicantDto.setPostId(applicantDto.getPostId());
        applicantDto.setUserId(applicantDto.getUserId());
        applicantDto.setState(applicantDto.getState());

        return applicantDto;
    }
}
