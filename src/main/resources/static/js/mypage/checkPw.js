// 비밀번호 보이기/숨기기 버튼 클릭 시 처리
document.getElementById("RtogglePassword").addEventListener("click", function() {
    var passwordField = document.getElementById("memPw");
    var eyeIcon = document.getElementById("ReyeIcon");

    if (passwordField.type === "password") {
        passwordField.type = "text"; // 비밀번호를 보이게 함
        eyeIcon.classList.remove("fa-eye");
        eyeIcon.classList.add("fa-eye-slash");
    } else {
        passwordField.type = "password"; // 비밀번호를 숨기게 함
        eyeIcon.classList.remove("fa-eye-slash");
        eyeIcon.classList.add("fa-eye");
    }
});

document.getElementById("checkPw").addEventListener("click", async function(){
    const memPw = document.getElementById("memPw").value.trim();
    if(memPw===""){
        alert("비밀번호를 입력해주세요");
        return;
    }
    try{
        const response = await fetch("/mypage/checkpw", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `memPw=${encodeURIComponent(memPw)}`
        });
        if(!response.ok){
            throw new Error("서버 오류");
        }
        const isValid = await response.json();

        if(isValid){
            window.location.href = "/mypage/changepw";
        }else{
            alert("비밀번호가 틀렸습니다. 다시 확인해주세요.");
            location.reload();
        }
    } catch(error){
        console.log("에러");
    }
});