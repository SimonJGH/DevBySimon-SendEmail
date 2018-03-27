package com.simon.email;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.et_send_email_address)
    EditText mEt_send_email_address;

    @ViewInject(R.id.et_send_email_password)
    EditText mEt_send_email_password;

    @ViewInject(R.id.et_receive_email_address)
    EditText mEt_receive_email_address;

    @ViewInject(R.id.et_email_subject)
    EditText mEt_email_subject;

    @ViewInject(R.id.et_email_content)
    EditText mEt_email_content;

    private String fromAddress, fromPassword, toAddress, subject, content;
    private List<File> files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);

    }

    @Event(value = {R.id.bt_send_email, R.id.bt_send_email_with_file})
    private void clickButton(View view) {
        switch (view.getId()) {
            case R.id.bt_send_email:
                // 腾讯企业邮箱
                fromAddress = mEt_send_email_address.getText().toString();
                fromPassword = mEt_send_email_password.getText().toString();
                toAddress = mEt_receive_email_address.getText().toString();
                subject = mEt_email_subject.getText().toString();
                content = mEt_email_content.getText().toString();
                if (TextUtils.isEmpty(fromAddress) || TextUtils.isEmpty(fromPassword) || TextUtils.isEmpty(toAddress) || TextUtils.isEmpty(subject) || TextUtils.isEmpty(content)) {
                    Toast.makeText(MainActivity.this, "数据格式异常,填写项不能为空。", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final boolean b = EmailUtils.getInstance().sendEmailMessages(fromAddress, fromPassword, toAddress, subject, content);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (b) {
                                    Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
                break;
            case R.id.bt_send_email_with_file:
                files = getFiles();
                if (files == null || files.isEmpty()) {
                    Toast.makeText(MainActivity.this, "上传文件不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 腾讯企业邮箱
                fromAddress = mEt_send_email_address.getText().toString();
                fromPassword = mEt_send_email_password.getText().toString();
                toAddress = mEt_receive_email_address.getText().toString();
                subject = mEt_email_subject.getText().toString();
                if (TextUtils.isEmpty(fromAddress) || TextUtils.isEmpty(fromPassword) || TextUtils.isEmpty(toAddress) || TextUtils.isEmpty(subject)) {
                    Toast.makeText(MainActivity.this, "数据格式异常,填写项不能为空。", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final boolean b = EmailUtils.getInstance().sendEmailMessagesWithFiles(fromAddress, fromPassword, toAddress, subject, files);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (b) {
                                    Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    /**
     * 获取要上传的文件
     *
     * @return
     */
    private List<File> getFiles() {
        List<File> fileList = new ArrayList<>();
        try {
            File[] files = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles();
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.contains(".xls")) {
                    fileList.add(file);
                }
            }
        } catch (Exception e) {

        }
        return fileList;
    }
}