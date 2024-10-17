document.addEventListener("DOMContentLoaded", function () {
    // 폼의 submit 이벤트 처리
    const form = document.querySelector("form");
    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        const loginRequest = {
            email: email,
            password: password
        };

        fetch("/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(loginRequest)
        })
        .then(response => {
            if (response.ok) {
                const accessToken = response.headers.get('Authorization');
                if (accessToken) {
                    localStorage.setItem('accessToken', accessToken);
                }
                alert("로그인되었습니다!");
                window.location.href = "/view/home";
            } else {
                alert("올바른 아이디와 비밀번호를 입력해주세요.");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("다시 입력해주세요.");
        });
    });

    // 모든 <a> 태그의 click 이벤트 처리
    document.querySelectorAll('.caption-made a').forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault();

            const email = this.getAttribute('data-email');
            const password = this.getAttribute('data-password');

            const loginRequest = {
                email: email,
                password: password
            };

            fetch("/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(loginRequest)
            })
            .then(response => {
                if (response.ok) {
                    const accessToken = response.headers.get('Authorization');
                    if (accessToken) {
                        localStorage.setItem('accessToken', accessToken);
                    }
                    alert("로그인되었습니다!");
                    window.location.href = "/view/home";
                } else {
                    alert("올바른 아이디와 비밀번호를 입력해주세요.");
                }
            });
        });
    });
});
