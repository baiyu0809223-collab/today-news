package com.pxxy.controller;

import com.pxxy.pojo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Tag(name = "AI 新闻生成", description = "基于大模型的新闻内容生成接口（支持多轮对话）")
@RestController
@RequestMapping("/ai")
@CrossOrigin
public class Ai_News {

    @Value("${ai.api-key}")
    private String apiKey;

    @Value("${ai.api-url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.create();

    /**
     * 新闻生成（支持多轮对话）
     */
    @Operation(
            summary = "AI 新闻生成",
            description = "根据多轮对话上下文生成新闻内容，返回大模型生成结果"
    )
    @PostMapping("/ai-news")
    public Mono<Result<Map<String, Object>>> generateNews(
            @Parameter(
                    description = "请求体，包含历史对话 messages（多轮对话）",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
{
  "messages": [
    { "role": "user", "content": "生成一条人工智能相关新闻" },
    { "role": "user", "content": "重点写商业应用" }
  ]
}
"""
                            )
                    )
            )
            @RequestBody Map<String, Object> body) {

        // 前端传入的历史对话
        List<Map<String, String>> userMessages =
                (List<Map<String, String>>) body.get("messages");

        // System Prompt（新闻约束）
        Map<String, String> systemMessage = Map.of(
                "role", "system",
                "content", """
你是一名专业新闻编辑。

【新闻要求】
- 新闻体裁：资讯新闻
- 语言：简体中文
- 风格：客观、中立、专业
- 字数：300 字左右
- 不要出现主观评价
- 直接输出新闻正文
"""
        );

        // 合并 system + 历史对话
        List<Map<String, String>> messages = new java.util.ArrayList<>();
        messages.add(systemMessage);
        messages.addAll(userMessages);

        // 通义千问请求体
        Map<String, Object> requestBody = Map.of(
                "model", "qwen-turbo",
                "input", Map.of(
                        "messages", messages
                ),
                "parameters", Map.of(
                        "result_format", "message",
                        "temperature", 0.6
                )
        );

        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)

                // ✅ 成功：统一返回 Result
                .map(Result::success)

                // ✅ 失败：统一错误格式（关键修复点）
                .onErrorResume(e ->
                        Mono.just(
                                Result.<Map<String, Object>>error(
                                        "AI 服务调用失败：" + e.getMessage()
                                )
                        )
                );
    }
}