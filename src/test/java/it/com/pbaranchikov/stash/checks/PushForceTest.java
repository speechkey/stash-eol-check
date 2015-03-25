package it.com.pbaranchikov.stash.checks;

import it.com.pbaranchikov.stash.checks.utils.WrappersFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;

/**
 * Unit tests for <code>git push --force</code> checks.
 * @author Pavel Baranchikov
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class PushForceTest extends AbstractGitCheck {

    private static final String TAG_GOOD = "goodRebaseTag";
    private static final String TAG_BAD = "badRebaseTag";

    public PushForceTest(WrappersFactory wrappersFactory) {
        super(wrappersFactory);
    }

    @Before
    public void prepareVariousBranch() {
        commitGood();
        commitBad();
        commitGood();
        getWorkspace().createTag(TAG_BAD);
        commitBad();
        getWorkspace().createTag(TAG_GOOD);
        commitGood();
        Assert.assertTrue(getWorkspace().push());
        getRepository().enableHook();
    }

    @Test
    public void testForceGood2Good() {
        getWorkspace().checkout(TAG_GOOD);
        commitGood();
        Assert.assertTrue(getWorkspace().pushForce(BRANCH_MASTER));
    }

    @Test
    public void testForceGood2Bad() {
        getWorkspace().checkout(TAG_GOOD);
        commitBad();
        Assert.assertFalse(getWorkspace().pushForce(BRANCH_MASTER));
    }

    @Test
    public void testForceBad2Bad() {
        getWorkspace().checkout(TAG_BAD);
        commitBad();
        Assert.assertFalse(getWorkspace().pushForce(BRANCH_MASTER));
    }

    @Test
    public void testForceBad2Good() {
        getWorkspace().checkout(TAG_BAD);
        commitGood();
        Assert.assertTrue(getWorkspace().pushForce(BRANCH_MASTER));
    }
}
