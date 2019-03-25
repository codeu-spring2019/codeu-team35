// Imports the Google Cloud client library
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;
import java.io.FileOutputStream;
import java.io.OutputStream;

/** Handles fetching and saving {@link Message} instances. */
@WebServlet("/a11y/tts")

public class TextToSpeechServlet extends HttpServlet {

 private TextToSpeechClient ttsClient;

 @Override
 public void init() {
   ttsClient = TextToSpeechClient.create();
 }

 /** Responses with a bytestream from the Google Cloud
 /* Text-to-Speech API
 **/
 @Override
 public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

   UserService userService = UserServiceFactory.getUserService();
   if (!userService.isUserLoggedIn()) {
     response.sendRedirect("/index.html");
     return;
   }

   String userMessage = Jsoup.clean(request.getParameter("message"), Whitelist.none());

   // Text to Speech API
   SynthesisInput input = SynthesisInput.newBuilder()
           .setText(userMessage)
           .build();

   // TODO(you): Fill in the gap here!
   // PS: consider the basic example and the Text-to-Speech documentation!

   response.setContentType("audio/mpeg"); 

   try (
     ServletOutputStream output = response.getOutputStream();
     InputStream input = getServletContext().getResourceAsStream(responseFromAPI); // Placeholder!
   ){ 
     byte[] buffer = new byte[2048];
     int bytesRead;    
     while ((bytesRead = input.read(buffer)) != -1) {
        output.write(buffer, 0, bytesRead);
     }
   }
 }
}