package com.example.server.follow.service;

import com.example.server.exception.BadRequestException;
import com.example.server.follow.dto.FollowInfo;
import com.example.server.follow.entity.Follow;
import com.example.server.follow.repository.FollowRepository;
import com.example.server.user.dto.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final ObjectMapper objectMapper;

    public boolean isFollow(int userId, int followerId) {
        return followRepository.findByUserIdAndFollowerId(userId, followerId).isPresent();
    }

    @Transactional
    public FollowInfo followUser(int userId, int followerId) {
        if (userId == followerId) {
            throw new BadRequestException();
        }
        if (isFollow(userId, followerId)) {  // 이미 팔로우 한 경우에는, 더 이상 팔로우 할 것이 없다.
            return null;
        }
        Follow follow = followRepository.save(new Follow(userId, followerId));

        return changeToFollowInfo(follow);
    }

    private static FollowInfo changeToFollowInfo(Follow follow) {
        return FollowInfo.builder()
                .followId(follow.getFollowId())
                .userId(follow.getUserId())
                .followerId(follow.getFollowerId())
                .followDatetime(follow.getFollowDatetime())
                .build();
    }

    @Transactional
    public boolean unfollowUser(int userId, int followerId) {
        Follow follow = followRepository.findByUserIdAndFollowerId(userId, followerId)
                .orElseThrow(() -> new BadRequestException());

        followRepository.delete(follow);
        return true;
    }

    /**
     * 나를 팔로우 하는 사람들 (팔로워) 조회 */
    public List<UserInfo> listFollower(int userId) {
        return followRepository.findFollowersByUserId(userId);
    }

    /**
     * 내가 팔로우 하는 사람들 (팔로잉) 조회 */
    public List<UserInfo> listFollowing(int userId) {
        return followRepository.findFollowingByUserId(userId);
    }
}
