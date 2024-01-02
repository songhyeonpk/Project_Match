package com.example.demo.repository.work;

import com.example.demo.dto.work.response.WorkPaginationResponseDto;
import com.example.demo.dto.work.response.WorkReadResponseDto;
import com.example.demo.model.milestone.QMilestone;
import com.example.demo.model.project.QProject;
import com.example.demo.model.project.QProjectMember;
import com.example.demo.model.user.QUser;
import com.example.demo.model.work.QWork;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WorkRepositoryImpl implements WorkRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    private final QProject qProject = QProject.project;
    private final QMilestone qMilestone = QMilestone.milestone;

    private final QWork qWork = QWork.work;

    @Override
    public WorkPaginationResponseDto findWorkByProjectIdAndMilestoneIdOrderByStartDateAsc(Long projectId, Long milestoneId, Pageable pageable) {
        QUser qUser = QUser.user;
        QProjectMember qProjectMember = QProjectMember.projectMember;

        // 업무 목록 조회
        List<WorkReadResponseDto> content = jpaQueryFactory
                .select(
                        Projections.constructor(
                                WorkReadResponseDto.class,
                                qWork.id,
                                qProject.id,
                                qMilestone.id,
                                qUser.nickname,
                                qProjectMember.user.nickname,
                                qWork.content,
                                qWork.startDate,
                                qWork.endDate,
                                qWork.progressStatus
                        )
                )
                .from(qWork)
                .leftJoin(qWork.project, qProject)
                .leftJoin(qWork.milestone, qMilestone)
                .leftJoin(qWork.assignedUserId, qUser)
                .leftJoin(qWork.lastModifiedMember, qProjectMember)
                .where(getPredicateForProjectAndMilestone(projectId, milestoneId))
                .orderBy(qWork.startDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 업무 전체 갯수 조회
        long totalPages = getTotalItemCount(projectId, milestoneId);

        return WorkPaginationResponseDto.of(content, totalPages);
    }

    private Long getTotalItemCount(Long projectId, Long milestoneId) {
        // 프로젝트 > 마일스톤 > 업무 전체 갯수 조회
        return jpaQueryFactory
                .select(qWork.count())
                .from(qWork)
                .where(getPredicateForProjectAndMilestone(projectId, milestoneId))
                .fetchOne();
    }

    private BooleanBuilder getPredicateForProjectAndMilestone(Long projectId, Long milestoneId) {
        BooleanBuilder builder = new BooleanBuilder();

        if(projectId != null) {
            builder.and(qProject.id.eq(projectId));
        }

        if(milestoneId != null) {
            builder.and(qMilestone.id.eq(milestoneId));
        }

        return builder;
    }
}