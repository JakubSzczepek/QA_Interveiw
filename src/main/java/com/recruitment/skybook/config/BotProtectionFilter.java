package com.recruitment.skybook.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Active protection against known AI/LLM crawlers and scrapers.
 * Blocks requests with known AI bot User-Agent strings returning 403 Forbidden.
 * Also adds X-Robots-Tag header to all responses.
 */
@Component
@Order(0) // Run before logging filter
public class BotProtectionFilter implements Filter {

    // Known AI bot user-agent patterns (case-insensitive)
    private static final List<Pattern> BLOCKED_BOTS = List.of(
            // OpenAI
            Pattern.compile("GPTBot", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ChatGPT-User", Pattern.CASE_INSENSITIVE),
            Pattern.compile("OAI-SearchBot", Pattern.CASE_INSENSITIVE),

            // Anthropic
            Pattern.compile("anthropic-ai", Pattern.CASE_INSENSITIVE),
            Pattern.compile("Claude-Web", Pattern.CASE_INSENSITIVE),
            Pattern.compile("ClaudeBot", Pattern.CASE_INSENSITIVE),

            // Google AI
            Pattern.compile("Google-Extended", Pattern.CASE_INSENSITIVE),

            // Meta
            Pattern.compile("FacebookBot", Pattern.CASE_INSENSITIVE),
            Pattern.compile("Meta-ExternalFetcher", Pattern.CASE_INSENSITIVE),
            Pattern.compile("Meta-ExternalAgent", Pattern.CASE_INSENSITIVE),

            // Common Crawl (trains many LLMs)
            Pattern.compile("CCBot", Pattern.CASE_INSENSITIVE),

            // Perplexity
            Pattern.compile("PerplexityBot", Pattern.CASE_INSENSITIVE),

            // Cohere
            Pattern.compile("cohere-ai", Pattern.CASE_INSENSITIVE),

            // Amazon
            Pattern.compile("Amazonbot", Pattern.CASE_INSENSITIVE),

            // Apple AI
            Pattern.compile("Applebot-Extended", Pattern.CASE_INSENSITIVE),

            // Bytedance
            Pattern.compile("Bytespider", Pattern.CASE_INSENSITIVE),

            // Other AI scrapers
            Pattern.compile("YouBot", Pattern.CASE_INSENSITIVE),
            Pattern.compile("Diffbot", Pattern.CASE_INSENSITIVE),
            Pattern.compile("Omgilibot", Pattern.CASE_INSENSITIVE),
            Pattern.compile("Timpibot", Pattern.CASE_INSENSITIVE),
            Pattern.compile("PetalBot", Pattern.CASE_INSENSITIVE),
            Pattern.compile("img2dataset", Pattern.CASE_INSENSITIVE),

            // Generic scraping tools
            Pattern.compile("Scrapy", Pattern.CASE_INSENSITIVE),
            Pattern.compile("python-requests.*bot", Pattern.CASE_INSENSITIVE)
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String userAgent = request.getHeader("User-Agent");

        // Block known AI bots
        if (userAgent != null && isBlockedBot(userAgent)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Forbidden\",\"message\":\"AI/bot access is not permitted\"}");
            return;
        }

        // Add X-Robots-Tag to discourage indexing/training
        response.setHeader("X-Robots-Tag", "noai, noimageai, noindex");

        chain.doFilter(request, response);
    }

    private boolean isBlockedBot(String userAgent) {
        for (Pattern pattern : BLOCKED_BOTS) {
            if (pattern.matcher(userAgent).find()) {
                return true;
            }
        }
        return false;
    }
}
