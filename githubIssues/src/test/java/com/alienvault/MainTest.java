package com.alienvault;

import org.junit.Test;

public class MainTest {

    @Test
    public void mainTest() throws Exception {

        String [] repositories = new String[] {
                "AlienVault-Labs/AlienVaultLabs",
                "AlienVault-Labs/OTX-Apps-TAXII",
                "AlienVault-Labs/OTX-Python-SDK",
                "aws/aws-cli",
                "aws/aws-sdk-java"
        };

        Main.main(repositories);
    }
}