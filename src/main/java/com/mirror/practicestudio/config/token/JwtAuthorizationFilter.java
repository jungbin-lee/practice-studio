//package com.mirror.practicestudio.config.token;
//
//import com.mirror.practicestudio.repository.UserRepository;
//import com.nimbusds.jose.Algorithm;
//import com.nimbusds.jwt.JWT;
//import net.minidev.json.JSONObject;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
//
//    private final UserRepository userRepository;
//
//
//    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
//        super(authenticationManager);
//        this.userRepository = userRepository;
//    }
//
//    //인증이나 권한이 필요한 주소요청이 있을때 해당 필터 작동.
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        System.out.println("인증이나 권한이 필요한 요청 시도");
//
//        String jwtHeader = request.getHeader("");
//
//        //header가 있는지 확인
//        if(jwtHeader == null || !jwtHeader.startsWith("Bearer ")){
//            chain.doFilter(request,response);
//            return;
//        }
//
////        String jwtToken = request.getHeader("").replace("");
//
//        // 토큰 만료됬을때 error잡기위해 try catch
//        try{
//            String username =
//                    JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("username").asString();
//
//            // JWT토큰 서명이 정상적으로 됨 (토큰 인증 ok)
//            if(username != null){
//                User user = userRepository.findByUsername(username);
//                PrincipalDetails principalDetails = new PrincipalDetails(user);
//                //임의로 token발급 -> username이 null이 아닌 경우라는 건 존재하는 회원이기떄문에.
//                System.out.println(principalDetails.getAuthorities());
//                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,null,principalDetails.getAuthorities());
//
//                //강제로 시큐리티 세션에 접근하여 authentication객체 저장
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//
//            //서명이 정상적으로 안됬을 시에 필터를 타게함.
//            chain.doFilter(request,response);
//
//        }catch (Exception e){
//
//            JSONObject json = new JSONObject();
//            json.put("message", "tokenExpired");
//            PrintWriter out = response.getWriter();
//            out.print(json);
//
//
//        }
//
//    }
//}
//
