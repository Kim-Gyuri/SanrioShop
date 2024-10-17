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
              } else {
                    alert("다시 입력해주세요.");
              }
        })
        .catch(error => {
              console.error("Error:", error);
              alert("다시 입력해주세요.");
        });
   });
});