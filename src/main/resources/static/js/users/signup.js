document.addEventListener("DOMContentLoaded", function () {

    const form = document.querySelector("form");
    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const nickname = document.getElementById("nickname").value;
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        const signupRequest = {
            nickname: nickname,
            email: email,
            password: password
        };

        fetch("/user/signUp", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(signupRequest)
        })
        .then(response => {
            if (response.status === 201) {
                alert("회원가입되었습니다!");
                window.location.href = "/view/login";
            } else if (response.status === 409) { // 중복된 이메일 또는 닉네임 예외 처리
                response.text().then(message => {
                    if (message === "중복된 이메일입니다.") {
                        alert("이미 사용 중인 이메일입니다.");
                    } else if (message === "중복된 닉네임입니다.") {
                        alert("이미 사용 중인 닉네임입니다.");
                    } else {
                        alert("알 수 없는 오류가 발생했습니다. 다시 시도해주세요.");
                    }
                });
            } else {
                alert("다시 입력해주세요.");
            }
        })
        .catch(error => {
            console.error("오류 발생:", error);
            alert("서버와의 통신 중 문제가 발생했습니다.");
        });
    });
});
