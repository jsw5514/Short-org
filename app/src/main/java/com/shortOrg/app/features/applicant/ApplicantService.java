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
        Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("게시글 불러오기 실패"));
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("사용자 불러오기 실패"));

        if(applicantRepository.existsByPostAndUser(post, user))
            throw new RuntimeException("이미 신청 완료한 게시글");

        Applicant applicant = new Applicant();

        applicant.setId(null);
        applicant.setUser(user);
        applicant.setPost(post);
        applicant.setState(ApplicantStatus.PENDING);

        applicantRepository.save(applicant);
    }

    public List<ApplicantDto> applicantShow(Long postId) {
        // entity -> dto
        Post post = postRepository.findById(postId).orElseThrow();
        List<Applicant> aList = applicantRepository.findByPostId(post.getId());
        List<ApplicantDto> dtos = new ArrayList<>();

        for (Applicant applicant : aList) {
            dtos.add(convertToDto(applicant));
        }

        return dtos;
    }

    private ApplicantDto convertToDto(Applicant applicant){
        ApplicantDto applicantDto = new ApplicantDto();

        applicantDto.setPostId(applicant.getPost().getId());
        applicantDto.setUserId(applicant.getUser().getId());
        applicantDto.setState(applicant.getState());

        return applicantDto;
    }

    @Transactional
    public void updateStatus(Long postId, String userId, ApplicantStatus state) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저 정보 불러오기 실패"));
        Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("게시글 불러오기 실패"));

        Applicant applicant = applicantRepository.findByPostAndUser(post, user);

        applicant.setState(state);
    }
}
