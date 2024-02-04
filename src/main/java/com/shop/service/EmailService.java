package com.shop.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;  // 의존성 주입을 통해 필요한 객체를 가져옴
    private static final String senderEmail= "xhzhdy1212@gmail.com";
    private static int number;  // 랜덤 인증 코드

    private static boolean checkValid;

    // 랜덤 인증 코드 생성
    public static void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    // 메일 양식 작성
    public MimeMessage createMail(String mail){
        createNumber();  // 인증 코드 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);   // 보내는 이메일
            message.setRecipients(MimeMessage.RecipientType.TO, mail); // 받는사람 이메일 설정
            message.setSubject("CRANK 이메일 인증코드");  // 제목 설정
            String body = String.join(
                    System.getProperty("line.separator"),
                    "<head>\n" +
                            "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                            "        <title>인증 코드</title>\n" +
                            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                            "    </head>\n" +
                            "\n" +
                            "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"550px\" style=\"border: 1px solid #d7d7d7; border-radius: 20px; text-align: center; font-family:'Malgun Gothic', '맑은 고딕'; letter-spacing: -0.04em; color: #333333;\">\n" +
                            "        <tr>\n" +
                            "            <td style=\"width: 40px;\"></td>\n" +
                            "            <td>\n" +
                            "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"470px;\">\n" +
                            "                    <tr><td style=\"height: 60px;\"></td></tr>\n" +
                            "                    <tr><td style=\"height: 18px;\"></td></tr>\n" +
                            "                    <tr><td><span style=\"font-size: 25px; font-weight: bold;\">인증코드</span></td></tr>\n" +
                            "                    <tr><td style=\"height: 34px;\"></td></tr>\n" +
                            "                    <tr><td style=\"height: 1px; background: #eaeaea;\"></td></tr>\n" +
                            "                    <tr><td style=\"height: 30px;\"></td></tr>" +
                            "                    <tr>\n" +
                            "                        <td>\n" +
                            "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"340px;\" style=\"margin: 0 auto;\">\n" +
                            "                                <tr>\n" +
                            "                                    <td>\n" +
                            "                                        <span style=\"font-size: 14px;\">\n" +
                            "                                            다음 인증코드를 입력하고 인증을 완료하세요."+
                            "                                        </span>\n" +
                            "                                    </td>\n" +

                            "                                        <span style=\"font-size: 20px; font-weight: 800px;\">\n" +
                                                                        number +
                            "                                         </span>\n" +
                            "                                         <br>" +
                            "                                </tr>\n<tr><td style=\"height: 30px;\"></td></tr>" +
                            "                            </table>\n" +
                            "                        </td>\n" +
                            "                    \n" +
                            "                </table>\n" +
                            "            </td>\n" +
                            "            <td style=\"width: 40px;\"></td>\n" +
                            "        </tr>\n" +
                            "    </table>",
                    "<p></p>"
            );
            message.setText(body,"UTF-8", "html");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    // 실제 메일 전송
    public String sendEmail(String email) throws MessagingException, UnsupportedEncodingException {
        /*
        // 메일 전송에 필요한 정보 설정
        MimeMessage message = createMail(email);
        // 실제 메일 전송
        javaMailSender.send(message);
         */
        System.out.println("이메일 인증번호"+number);
        // 인증 코드 반환
        return number+"";
    }

    public boolean checkNumber(String num){
        int checkNumber = Integer.parseInt(num);
        if(checkNumber==number){
            checkValid = true;
            return true;
        }
            return false;
    }

    public boolean validCheck() {
        if(checkValid){
            checkValid = false;
            return true;
        }
        return false;
    }
}