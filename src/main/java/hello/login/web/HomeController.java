package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    // 로그인 정보까지 처리 되는 홈 화면
    @GetMapping("/")
    public String homeLogin(
            // 로그인이 안된 경우에도 홈 화면을 보여줘야 하므로 required = false
            @CookieValue(name = "memberId", required = false) Long memberId,
            Model model
    ) {

        // 토큰 없음 - 로그인 안된 사용자 화면
        if (memberId == null) {
            return "home";
        }

        // 토큰 값이 유효하지 않음 - 로그인 안된 사용자 화면
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        // 로그인 된 사용자 화면
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
