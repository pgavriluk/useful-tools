package connections;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import helpers.MongoDBConfigurationsHelper;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class MongoDbConnection {

    private static final int MONGO_DB_PORT = 27017;
    private static final int LOCAL_PORT = 8989;
    private static MongoDbConnection INSTANCE = null;
    private static MongoClient mongoClient = null;
    private static Session sshSession = null;

    private MongoDbConnection() {
    }

    public synchronized static MongoDbConnection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MongoDbConnection();
        }

        return INSTANCE;
    }

    @Step("Get Connection to Mongo DB")
    public synchronized MongoClient getConnection() {
        if (mongoClient == null) {
            String address = MongoDBConfigurationsHelper.getInstance().getAddress();
            try {
                if (address.equalsIgnoreCase("localhost")) {
                    openSshSession();

                    String database = MongoDBConfigurationsHelper.getInstance().getDatabase();
                    String dbUsername = MongoDBConfigurationsHelper.getInstance().getDbUsername();
                    String dbPassword = MongoDBConfigurationsHelper.getInstance().getDbPassword();

                    MongoCredential credential = MongoCredential.createCredential(dbUsername, database, dbPassword.toCharArray());
                    mongoClient = new MongoClient(new ServerAddress(address, LOCAL_PORT), credential, MongoClientOptions.builder().build());
                } else {
                    mongoClient = new MongoClient(address, MONGO_DB_PORT);
                }
                log.info("Connected to Mongo DB at '{}'", address + ":" + MONGO_DB_PORT);
            } catch (Exception e) {
                log.error("Could not connect to Mongo DB at '{}'", address + ":" + MONGO_DB_PORT);
            }
        }

        return mongoClient;
    }

    public synchronized void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }

        if (sshSession != null) {
            try {
                sshSession.delPortForwardingL(LOCAL_PORT);
            } catch (JSchException e) {
                e.printStackTrace();
            }
            sshSession.disconnect();
        }
    }

    private void openSshSession() {
        try {
            String sshHost = MongoDBConfigurationsHelper.getInstance().getSshHost();
            String sshUsername = MongoDBConfigurationsHelper.getInstance().getSshUsername();
            String sshPassword = MongoDBConfigurationsHelper.getInstance().getSshPassword();
            String remoteHost = "127.0.0.1";
            int sshPort = 22;

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            sshSession = jsch.getSession(sshUsername, sshHost, sshPort);
            sshSession.setPassword(sshPassword);
            sshSession.setConfig(config);
            sshSession.connect();
            sshSession.setPortForwardingL(LOCAL_PORT, remoteHost, MONGO_DB_PORT);
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

}
