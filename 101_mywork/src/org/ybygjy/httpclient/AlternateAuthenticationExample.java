package org.ybygjy.httpclient;
/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//httpclient/src/examples/AlternateAuthenticationExample.java,v 1.3 2004/06/12 22:47:23 olegk Exp $
 * $Revision: 155418 $
 * $Date: 2005-02-26 08:01:52 -0500 (Sat, 26 Feb 2005) $
 * ====================================================================
 *
 *  Copyright 1999-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * [Additional notices, if required by prior licensing conditions]
 *
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;


/**
 * <p>A simple example that uses alternate authentication scheme selection
 * if several authentication challenges are returned.
 * </p>
 * 
 * <p>Per default HttpClient picks the authentication challenge in the following
 *  order of preference: NTLM, Digest, Basic. In certain cases it may be desirable to
 *  force the use of a weaker authentication scheme. 
 * </p>
 *
 * @author Oleg Kalnichevski
 */
public class AlternateAuthenticationExample {

    /**
     * Constructor for BasicAuthenticatonExample.
     */
    public AlternateAuthenticationExample() {
        super();
    }

    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        client.getState().setCredentials(
            new AuthScope("myhost", 80, "myrealm"),
            new UsernamePasswordCredentials("username", "password"));
        // Suppose the site supports several authetication schemes: NTLM and Basic
        // Basic authetication is considered inherently insecure. Hence, NTLM authentication
        // is used per default

        // This is to make HttpClient pick the Basic authentication scheme over NTLM & Digest
        List authPrefs = new ArrayList(3);
        authPrefs.add(AuthPolicy.BASIC);
        authPrefs.add(AuthPolicy.NTLM);
        authPrefs.add(AuthPolicy.DIGEST);
        client.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);

        GetMethod httpget = new GetMethod("http://localhost:8080/index.html");

        try {
            int status = client.executeMethod(httpget);
            // print the status and response
            System.out.println(httpget.getStatusLine());
            System.out.println(httpget.getResponseBodyAsString());
        } finally {
            // release any connection resources used by the method
            httpget.releaseConnection();
        }            
    }
}
