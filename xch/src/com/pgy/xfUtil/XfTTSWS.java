package com.pgy.xfUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 语音合成流式 WebAPI 接口调用示例 接口文档（必看）：https://www.xfyun.cn/doc/tts/online_tts/API.html
 * 语音合成流式WebAPI 服务，发音人使用方式：登陆开放平台https://www.xfyun.cn/后，到控制台-我的应用-语音合成-添加试用或购买发音人，添加后即显示该发音人参数值
 * 错误码链接：https://www.xfyun.cn/document/error-code （code返回错误码时必看）
 * 小语种需要传输小语种文本、使用小语种发音人vcn、ent=mtts、tte=unicode以及修改文本编码方式
 * @author iflytek
 */

public class XfTTSWS {
	
	private static Logger logger = LogManager.getLogger(XfTTSWS.class);
	
	private static final String fileUrl = "D:/javaWeb/wx_audio/"; 
	private static final String serverUrl = "http://move.ehai.xyz/wx_audio/";
	
    private static final String hostUrl = "https://tts-api.xfyun.cn/v2/tts"; //http url 不支持解析 ws/wss schema
    private static final String appid = "5e1d6ccd";//到控制台-语音合成页面获取
    private static final String apiSecret = "7875fc97abd40fd4b974c089d24401ea";//到控制台-语音合成页面获取
    private static final String apiKey = "cb27ffc6759669d4cde8d683fb3f5a19";//到控制台-语音合成页面获取
    //private static final String text = "别人认为重要的事情，你不能把它作为实现自己目标的依据。只有通过你自己的实践经历与认真思考之后，才知道什么东西对你最好，什么事情对你最重要。";
    public static final Gson json = new Gson();
    
    public static String pcmSrc = "";
    
    
    
    
    public static String textToAudio(String text) throws FileNotFoundException {
    	
    	// 构建鉴权url
        String authUrl = "";
		try {
			authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
        OkHttpClient client = new OkHttpClient.Builder().build();
        //将url中的 schema http://和https://分别替换为ws:// 和 wss://
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        // 存放音频的文件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String date = sdf.format(new Date());
        
        pcmSrc = fileUrl + date + ".pcm";
        
        File f = new File(fileUrl + date + ".pcm");
        if (!f.exists()) {
            try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        FileOutputStream os = new FileOutputStream(f);
        WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                try {
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //发送数据
                JsonObject frame = new JsonObject();
                JsonObject business = new JsonObject();
                JsonObject common = new JsonObject();
                JsonObject data = new JsonObject();
                // 填充common
                common.addProperty("app_id", appid);
                //填充business
                business.addProperty("aue", "raw");
                business.addProperty("tte", "UTF8");//小语种必须使用UNICODE编码
                business.addProperty("ent", "intp65");
                business.addProperty("vcn", "aisbabyxu");//到控制台-我的应用-语音合成-添加试用或购买发音人，添加后即显示该发音人参数值，若试用未添加的发音人会报错11200
                business.addProperty("pitch", 50);
                /*business.addProperty("speed", 45);
                business.addProperty("spd", 45);*/
                //加背景音
                business.addProperty("bgs", 1);
                business.addProperty("pitch", 40);
                
                //填充data
                data.addProperty("status", 2);//固定位2
                try {
                    data.addProperty("text", Base64.getEncoder().encodeToString(text.getBytes("utf8")));
					//使用小语种须使用下面的代码，此处的unicode指的是 utf16小端的编码方式，即"UTF-16LE"”
                    //data.addProperty("text", Base64.getEncoder().encodeToString(text.getBytes("UTF-16LE")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //填充frame
                frame.add("common", common);
                frame.add("business", business);
                frame.add("data", data);
                webSocket.send(frame.toString());
            }
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                //处理返回数据
                /*返回数据打印日志，太长了关掉*/
                /*logger.info("receive=>" + text);*/
                ResponseData resp = null;
                try {
                    resp = json.fromJson(text, ResponseData.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (resp != null) {
                    if (resp.getCode() != 0) {
                    	logger.error("error=>" + resp.getMessage() + " sid=" + resp.getSid());
                        return;
                    }
                    if (resp.getData() != null) {
                        String result = resp.getData().audio;
                        byte[] audio = Base64.getDecoder().decode(result);
                        try {
							os.write(audio);
							os.flush();
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
                        
                        
                        if (resp.getData().status == 2) {
                            
                        	logger.info("session end ");
                        	
                            webSocket.close(1000, "");
                            try {
                                os.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }
            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                logger.info("socket closing");
            }
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                logger.info("socket closed");
                String pcmSrc = fileUrl + date + ".pcm";
                
                String mp3Src = fileUrl + date + ".mp3";
        		
        		// todo  resp.data.status ==2 说明数据全部返回完毕，可以关闭连接，释放资源
            	try {
        			pcmToMp3(pcmSrc, mp3Src);
        		} catch (Exception e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
            	logger.info("合成的音频文件保存在：" + mp3Src);
            	
            }
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                logger.info("connection failed");
            }
        });
    	
        return date;
        
        
        
    }
    
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n").//
                append("date: ").append(date).append("\n").//
                append("GET ").append(url.getPath()).append(" HTTP/1.1");
        Charset charset = Charset.forName("UTF-8");
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        String authorization = String.format("hmac username=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(charset))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();
        return httpUrl.toString();
    }
    public static class ResponseData {
        private int code;
        private String message;
        private String sid;
        private Data data;
        public int getCode() {
            return code;
        }
        public String getMessage() {
            return this.message;
        }
        public String getSid() {
            return sid;
        }
        public Data getData() {
            return data;
        }
    }
    
    
    public static class Data {
        private int status;  //标志音频是否返回结束  status=1，表示后续还有音频返回，status=2表示所有的音频已经返回
        private String audio;  //返回的音频，base64 编码
        private String ced;  // 合成进度
    }
    
    /**
     * pcm转为MP3
     * @param src 源文件
     * @param target 转换后的文件
     * @throws Exception
     */
    public static void pcmToMp3(String src, String target) throws Exception {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(target);

        // 计算长度
        byte[] buf = new byte[1024 * 4];
        int size = fis.read(buf);
        int PCMSize = 0;
        while (size != -1) {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();

        //填入参数，比特率等等。这里用的是16位单声道 8000 hz
        WaveHeader header = new WaveHeader(100);
        //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = PCMSize + (44 - 8);
        header.fmtHdrLeth = 16;
        header.bitsPerSample = 16;
        header.channels = 1;
        header.formatTag = 0x0001;
        header.avgBytesPerSec = 8000;
        header.blockAlign = (short)(header.channels * header.bitsPerSample / 8);
        header.avgBytesPerSec = header.blockAlign * header.avgBytesPerSec;
        header.dataHdrLeth = PCMSize;

        byte[] h = header.getHeader();

        assert h.length == 44; // WAV标准，头部应该是44字节
        // write header
        fos.write(h, 0, h.length);
        // write data stream
        fis = new FileInputStream(src);
        size = fis.read(buf);
        while (size != -1) {
            fos.write(buf, 0, size);
            size = fis.read(buf);
        }
        fis.close();
        fos.close();
        logger.info("Convert OK!");
    }
    
}