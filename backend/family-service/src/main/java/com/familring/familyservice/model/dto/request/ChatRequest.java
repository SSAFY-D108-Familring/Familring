package com.familring.familyservice.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private Long senderId; // 발신자 id
    private String content; // 채팅 내용
    @JsonProperty("isVote")
    private boolean isVote; // 투표 여부
    private String voteTitle; // 투표 제목

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isVote() { // 정확히 `isVote()`으로 설정
        return isVote;
    }

    public void setVote(boolean isVote) { // 정확히 `setVote()`으로 설정
        this.isVote = isVote;
    }

    public String getVoteTitle() {
        return voteTitle;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }
}
