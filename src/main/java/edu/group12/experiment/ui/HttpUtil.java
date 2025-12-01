// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package edu.group12.experiment.ui;

import com.sun.net.httpserver.HttpExchange;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class HttpUtil {
   private HttpUtil() {
   }

   public static void sendHtml(HttpExchange exchange, String body) throws IOException {
      byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
      exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
      exchange.sendResponseHeaders(200, (long)bytes.length);
      OutputStream os = exchange.getResponseBody();

      try {
         os.write(bytes);
      } catch (Throwable var7) {
         if (os != null) {
            try {
               os.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (os != null) {
         os.close();
      }

   }

   public static void redirect(HttpExchange exchange, String location) throws IOException {
      exchange.getResponseHeaders().set("Location", location);
      exchange.sendResponseHeaders(302, -1L);
      exchange.close();
   }

   public static Map<String, String> readFormParams(HttpExchange exchange) throws IOException {
      Map<String, String> map = new HashMap();
      String query = exchange.getRequestURI().getRawQuery();
      if (query != null && !query.isEmpty()) {
         parseUrlEncodedIntoMap(query, map);
      }

      byte[] body = readAll(exchange.getRequestBody());
      if (body.length > 0) {
         parseUrlEncodedIntoMap(new String(body, StandardCharsets.UTF_8), map);
      }

      return map;
   }

   private static void parseUrlEncodedIntoMap(String s, Map<String, String> out) {
      String[] var2 = s.split("&");
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String pair = var2[var4];
         int i = pair.indexOf(61);
         String key;
         if (i >= 0) {
            key = URLDecoder.decode(pair.substring(0, i), StandardCharsets.UTF_8);
            String val = URLDecoder.decode(pair.substring(i + 1), StandardCharsets.UTF_8);
            out.put(key, val);
         } else if (!pair.isEmpty()) {
            key = URLDecoder.decode(pair, StandardCharsets.UTF_8);
            out.put(key, "");
         }
      }

   }

   private static byte[] readAll(InputStream in) throws IOException {
      InputStream var1 = in;

      byte[] var5;
      try {
         ByteArrayOutputStream buf = new ByteArrayOutputStream();

         try {
            byte[] tmp = new byte[4096];

            while(true) {
               int n;
               if ((n = in.read(tmp)) <= 0) {
                  var5 = buf.toByteArray();
                  break;
               }

               buf.write(tmp, 0, n);
            }
         } catch (Throwable var8) {
            try {
               buf.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         buf.close();
      } catch (Throwable var9) {
         if (in != null) {
            try {
               var1.close();
            } catch (Throwable var6) {
               var9.addSuppressed(var6);
            }
         }

         throw var9;
      }

      if (in != null) {
         in.close();
      }

      return var5;
   }
}
