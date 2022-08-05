package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    // 로그인 정보까지 처리 되는 홈 화면
    @GetMapping("/")
    public String homeLoginVArgumentResolver(@Login Member loginMember, Model model) {

        if (loginMember == null) return "home";

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3Spring(
            // 이미 로그인 된 사용자를 찾을 때 이렇게 사용. 세션은 생성하지 않는다.
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            Model model) {

        if (loginMember == null) return "home";

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        // session 은 메모리를 사용하기 때문에 꼭 필요한 경우에만 생성하자.
        HttpSession session = request.getSession(false);
        if (session == null) return "home";

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) return "home";

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request);

        if (member == null) return "home";

        model.addAttribute("member", member);
        return "loginHome";
    }

    //@GetMapping("/")
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
