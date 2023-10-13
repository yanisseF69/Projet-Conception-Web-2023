package fr.univlyon1.m1if.m1if03.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 Class EntityTag Filter.
 */
@WebFilter(filterName = "EntityTagFilter", urlPatterns = "*")
public class ETagFilter extends HttpFilter {

    private HttpFilter config;


    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        CharArrayWriter charArrayWriter = new CharArrayWriter();

        chain.doFilter(request, response);

        String content = charArrayWriter.toString();
        String obfuscatedETag = generateETag(content);

        String ifNoneMatch = request.getHeader("If-None-Match");
        if (ifNoneMatch != null && ifNoneMatch.equals(obfuscatedETag)) {
            // Si les ETags correspondent, renvoyez 304 Not Modified
            response.reset();
            response.setStatus(304);
        } else {
            // Ajoutez l'ETag à la réponse
            response.addHeader("ETag", obfuscatedETag);

            // Écrivez le contenu dans la réponse
            response.getWriter().write(content);
        }
    }

    @Override
    public void destroy() {
        this.config = null;
    }

    private String generateETag(String content) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(content.getBytes());
            String obfuscatedETag = Arrays.toString(bytes);
            return obfuscatedETag;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
