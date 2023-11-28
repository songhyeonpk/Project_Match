package com.example.demo.service.project;

import com.example.demo.constant.AlertType;
import com.example.demo.constant.ProjectMemberStatus;
import com.example.demo.global.exception.customexception.ProjectMemberCustomException;
import com.example.demo.model.alert.Alert;
import com.example.demo.model.project.Project;
import com.example.demo.model.project.ProjectMember;
import com.example.demo.model.project.ProjectMemberAuth;
import com.example.demo.model.user.User;
import com.example.demo.repository.project.ProjectMemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService{
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectMember toProjectMemberEntity(Project project, User user, ProjectMemberAuth projectMemberAuth){
        return ProjectMember.builder()
                .project(project)
                .user(user)
                .projectMemberAuth(projectMemberAuth)
                .status(ProjectMemberStatus.PARTICIPATING)
                .position(project.getUser().getPosition())
                .build();
    }

    @Override
    public ProjectMember findById(Long id) {
        return projectMemberRepository.findById(id).orElseThrow(() -> ProjectMemberCustomException.NOT_FOUND_PROJECT_MEMBER);
    }

    public List<ProjectMember> findProjectsMemberByProject(Project project){
        return projectMemberRepository.findProjectsMemberByProject(project).orElseThrow(() -> ProjectMemberCustomException.NOT_FOUND_PROJECT_MEMBER);
    }

    @Override
    public ProjectMember save(ProjectMember projectMember) {
        return projectMemberRepository.save(projectMember);
    }


    /**
     * 프로젝트 멤버 탈퇴 알림 보내기
     *
     * @param projectMemberId
     */
//    public void sendWithdrawlAlert(Long projectMemberId) {
//        ProjectMember projectMember =
//                projectMemberRepository
//                        .findById(projectMemberId)
//                        .orElseThrow(() -> ProjectMemberCustomException.NOT_FOUND_PROJECT_MEMBER);
//
//        Alert alert =
//                Alert.builder()
//                        .project(projectMember.getProject())
//                        .checkUser(projectMember.getProject().getUser())
//                        .applyUser(projectMember.getUser())
//                        .content("프로젝트 탈퇴")
//                        .type(AlertType.WITHDRWAL)
//                        .checked_YN(false)
//                        .build();
//
//        alertRepository.save(alert);
//    }

    /**
     * 프로젝트 멤버 탈퇴 수락하기
     *
     * @param projectMemberId
     */
//    public void withdrawlConfirm(Long projectMemberId) {
//        ProjectMember projectMember =
//                projectMemberRepository
//                        .findById(projectMemberId)
//                        .orElseThrow(() -> ProjectMemberCustomException.NOT_FOUND_PROJECT_MEMBER);
//        projectMemberRepository.delete(projectMember);
//    }

    /**
     * 프로젝트 멤버 강제 탈퇴하기.
     *
     * @param projectMemberId
     */
//    public void withdrawlForce(Long projectMemberId) {
//        ProjectMember projectMember =
//                projectMemberRepository
//                        .findById(projectMemberId)
//                        .orElseThrow(() -> ProjectMemberCustomException.NOT_FOUND_PROJECT_MEMBER);
//        projectMemberRepository.delete(projectMember);
//    }
}