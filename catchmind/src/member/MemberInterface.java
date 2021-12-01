package member;

public interface MemberInterface {
	// 로그인  <-> 회원가입 화면 전환
	public void setHyperLink();
	// login UI 초기화
	public void initLoginUI();
	// join UI 초기화
	public void initJoinUI();
	// login event 초기화
	public void setLoginEvent();
	// join event 초기화
	public void setJoinEvent();
	// 아이디 중복체크 UI 변경
	public void setJoinIDCheck(boolean isChecked);
	// 회원가입 성공 유무
	public void setJoinCheck(boolean isSuccess);
	// 로그인 성공 여부 check
	public void setLoginCheck();
	// 게임화면 오픈
	public void showGameRoom();
	
}








