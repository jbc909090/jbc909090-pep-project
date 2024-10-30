package Service;

import Model.Account;
import Model.Message;
import DAO.DAOSocialMedia;
import java.util.List;

public class ServiceSocialMedia {
    /*
     * service do all 8 task by taking the request body from controller functions and use DAO functions
     * 1. Input Account no ID, Output Account with id or null DONE
     * 2. Input Account no ID, Output Account with id or null DONE
     * 3. Input Message no ID, Output Message with id or null DONE
     * 4. Input none, Output all Messages DONE
     * 5. Input Message ID, Output Message with ID or null DONE
     * 6. Input Message ID, Output Message with ID or null DONE
     * 7. Input Message text and ID, Output Message with ID or null
     * 8. Input Account ID, Output all Messages by a user potentially null
     */
    DAOSocialMedia dao;
    public ServiceSocialMedia() {
        dao = new DAOSocialMedia();
    }
    public ServiceSocialMedia(DAOSocialMedia dao){
        this.dao = dao;
    }
    public Account addAccount(Account account) {
        if (!account.getUsername().isBlank()) {
            //username is not blank
            if (account.getPassword().length() > 3) {
                //password is at least 4 characters long
                if (!dao.checkForExistingUsername(account.getUsername())){
                    //username does NOT exist
                    return dao.insertAccount(account);
                }
            }
        }
        //else do not create a new account
        return null;
    }
    public Account login(Account account) {
        return dao.getAccountByLogin(account);
    }
    public Message addMessage(Message message){
        if (!message.getMessage_text().isBlank()) {
            if (message.getMessage_text().length() <= 255) {
                if (dao.getAccountByID(message.getPosted_by()) != null) {
                    return dao.insertMessage(message);
                }
            }
        }
        return null;
    }
    
    public List<Message> getAllMessages() {
        return dao.getAllMessages();
    }
    public Message getMessageByID(int id) {
        return dao.getMessageByID(id);
    }
    public Message deleteMessage(int id) {
        Message deleted = dao.getMessageByID(id);
        if (deleted != null) {
            dao.deleteMessage(id);
        }
        return deleted;
    }
    public Message updateMessage(int id, String text) {
        if (dao.getMessageByID(id) != null) {
            if (!text.isBlank()) {
                if (text.length() <= 255) {
                    dao.updateMessage(id, text);
                    return dao.getMessageByID(id);
                }
            }
        }
        return null;
    }
    public List<Message> getAllUserMessages(int id) {
        return dao.getMessagesByAccount(id);
    }
}
