package net.itsrelizc.tests;

import org.bukkit.plugin.Plugin;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

public class Tests {
	private ServerMock server;
    private Plugin plugin;

    @Before
    void setUp() {
    	server = MockBukkit.mock();

    }

    @After
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void itemEval() {
        Assert.assertTrue(false);
    }
}
