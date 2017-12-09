package controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Ban;
import domain.Channel;
import domain.Email;
import domain.Log;
import domain.Message;
import domain.Session;
import domain.User;
import domain.Sms;
import repositories.BanRepository;
import repositories.ChannelRepository;
import repositories.MessageRepository;
import repositories.SessionRepository;
import repositories.UserRepository;
import services.AsyncMail;
import services.AsyncSms;
import services.BanService;
import services.ChannelService;
import services.LogService;
import services.MessageService;
import services.SessionService;
import services.UserService;

@Controller
@SessionAttributes({"login"})
public class SendController {
	private static final String SMS_SENDER = "+15752147992";
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private ChannelRepository channelRepository;
	@Autowired
	private LogService logService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private SessionRepository sessionRepository;
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private AsyncMail asyncMail;
	@Autowired
	private AsyncSms asyncSms;
	@Autowired
	private BanService banService;
	@Autowired
	private BanRepository banRepository;
	
	
	@RequestMapping(value = "/test/send", method = RequestMethod.POST)
    public String send( @RequestParam (value="usermsg") String text,
    					@RequestParam (value="sessionId") String sessionId,
    					@RequestParam (value="channelId") String channelId
						, Locale locale, ModelMap model) {
		Session session = sessionService.getById(sessionId);
		Channel channel = channelService.getById(channelId);
		String senderId = ((User)model.get("login")).getId();
		Message newmsg = new Message(1,text,senderId,channelId, sessionId);


		for(String memberid : channel.getMembersList()) {
			if(!(memberid.equals(senderId))) {
				User receiver = userService.getById(memberid);
				if(channel.getEmail()) {
					try {
						asyncMail.sendMail(receiver.getEmail(),text);
					}catch( Exception e ){
						// catch error
					}
					Log newlog = new Log("User ID: " + senderId + "Sent an e-Mail to user ID: " + receiver.getId() + "through channel ID: " + channelId + " with session ID: " + sessionId);
					logService.saveOrUpdate(newlog);
				}
				if(channel.getSms()) {
					try {
						asyncSms.sendSms(receiver.getPhoneNumber(),SMS_SENDER,text);
					}catch( Exception e ){
						// catch error
					}
					Log newlog = new Log("User ID: " + senderId + "Sent an SMS to user ID: " + receiver.getId() + "through channel ID: " + channelId + " with session ID: " + sessionId);
					logService.saveOrUpdate(newlog);
				}
			}
		}
		
		newmsg = messageService.saveOrUpdate(newmsg);
		session.addMessage(newmsg.getId());
		sessionService.saveOrUpdate(session);
		Log newlog = new Log("Sent a new message to channel with ID: " + channelId + " with session ID: " + sessionId);
		logService.saveOrUpdate(newlog);
		
        return "redirect:/test/channel/"+session.getChannelId();
    }
}