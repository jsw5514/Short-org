package com.shortOrg.app.features.applicant;

import com.shortOrg.app.domain.Applicant;
import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.repository.ApplicantRepository;
import com.shortOrg.app.repository.PostRepository;
import com.shortOrg.app.repository.UserRepository;
import com.shortOrg.app.features.applicant.dto.ApplicantResponse;
import com.shortOrg.app.shared.enumerate.ApplicantStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
            throw new IllegalArgumentException("이미 신청 완료한 게시글");

        if(post.getWriter().getId().equals(userId))
            throw new RuntimeException("본인 글에는 신청 불가");

        Applicant applicant = new Applicant();

        applicant.setId(null);
        applicant.setUser(user);
        applicant.setPost(post);
        applicant.setState(ApplicantStatus.PENDING);

        applicantRepository.save(applicant);
    }

    public List<ApplicantResponse> applicantShow(Long postId) {
        // entity -> dto
        Post post = postRepository.findById(postId).orElseThrow();
        List<Applicant> aList = applicantRepository.findByPostId(post.getId());
        List<ApplicantResponse> dtos = new ArrayList<>();

        for (Applicant applicant : aList) {
            dtos.add(convertToDto(applicant));
        }

        return dtos;
    }

    private ApplicantResponse convertToDto(Applicant applicant){
        ApplicantResponse applicantResponse = new ApplicantResponse();

        applicantResponse.setPostId(applicant.getPost().getId());
        applicantResponse.setUserId(applicant.getUser().getId());
        applicantResponse.setState(applicant.getState());

        return applicantResponse;
    }

    // 요청 승인/거절
    @Transactional
    public void updateStatus(Long postId, String userId, ApplicantStatus state, String writerId) {
        //state 유효성 판단
        switch (state) {
            case MEMBER, REJECTED -> log.debug("유효한 모임 신청 처리");
            default -> {
                log.error("잘못된 모임 신청 처리입니다. 요청된 상태 변경: {}",ApplicantStatus.toString(state));
                throw new IllegalArgumentException("잘못된 모임 신청처리입니다. 유효한 요청은 MEMBER, REJECTED 입니다.");
            }
        }
        Applicant applicant = applicantRepository.findByPostIdAndUserId(postId, userId).orElseThrow(()->new IllegalArgumentException("처리할 요청을 찾지 못했습니다."));
        Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException(""));
        if(!post.getWriter().getId().equals(writerId))
            throw new RuntimeException("글쓴이와 수락하는 사람이 일치하지 않음");

        applicant.setState(state);
        applicantRepository.save(applicant);
    }

    public void cancelApply(Long postId, String userId) {
        log.debug("모임 요청 취소: postId = {}, userId = {}", postId, userId);
        long deletedCount = applicantRepository.deleteByPost_IdAndUser_Id(postId, userId);
        if(deletedCount == 0) {
            log.error("모임 신청 취소 실패: 요청을 찾을 수 없음");
            throw new IllegalArgumentException("요청을 찾을수 없음");
        }
    }
}
