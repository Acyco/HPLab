package cn.acyco.lab.config;

import cn.acyco.lab.Reference;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.nio.charset.StandardCharsets;

import com.google.common.collect.Maps;
import com.google.common.io.Files;

import java.util.*;

/**
 * @author Acyco
 * @create 2020-01-28 16:59
 */
public class ServerConfig<K, V extends ConfigEntry<K>> {
    File saveFile =new File(Reference.CONFIG_FILE);
    Gson gson;
    private Map<String, V> values = Maps.newHashMap();
    private static ServerConfig instance = new ServerConfig();
    private static final ParameterizedType type = new ParameterizedType() {
        public Type[] getActualTypeArguments() {
            return new Type[]{ServerConfigEntry.class};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }
    };

    private ServerConfig() {
                      loadFile();
    }

    public static ServerConfig getInstance() {     //主线程加载，不存在线程安全问题
        return instance;
    }


    public void loadFile() {
        GsonBuilder gsonBuilder = (new GsonBuilder()).setPrettyPrinting();
        gsonBuilder.registerTypeHierarchyAdapter(ServerConfig.class, new ServerConfig.Serializer());
        this.gson = gsonBuilder.create();

          /*    if (this.values.size() == 0) {
            try {
                read();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

    }

    protected void read() throws FileNotFoundException {
        if (this.saveFile.exists()) {

            BufferedReader bufferedReader = null;
            try {
                bufferedReader = Files.newReader(this.saveFile, StandardCharsets.UTF_8);
                JsonReader jsonReader = new JsonReader(bufferedReader);
                Collection<ServerConfigEntry<K>> collection = (Collection<ServerConfigEntry<K>>) this.gson.getAdapter(TypeToken.get(type)).read(jsonReader);
                if (collection != null) {
                    this.values.clear();
                    Iterator iterator = collection.iterator();
                    while (iterator.hasNext()) {
                        final ServerConfigEntry serverConfigEntry = (ServerConfigEntry) iterator.next();
                        if (serverConfigEntry.getValue() != null) {
                            this.values.put(this.getObjectKey((K) serverConfigEntry.getValue()), (V) serverConfigEntry);

                        }
                    }
                }
                if (this.values.size() == 0) {
                    ServerConfigEntry serverConfigEntry = new ServerConfigEntry("default", "default", "server.jar", "xxxx", "java -jar server.jar");
                    this.addEntry((V) serverConfigEntry);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }


    }

    /* private void readJson() {
         try {
             BufferedReader bufferedReader = new BufferedReader(
                     new InputStreamReader(new FileInputStream(saveFile)));
             // 创建StringBuffer
             StringBuffer stringBuffer = new StringBuffer();
             String temp = "";
             while ((temp = bufferedReader.readLine()) != null) {
                 stringBuffer.append(temp);
             }
             String string = stringBuffer.toString();
             System.out.println(string);
             Gson tempGson = new Gson();
             final Type type = new TypeToken<ArrayList<V>>() {}.getType();
             final ArrayList arrayList = tempGson.fromJson(string, type);
             Map<String,V> maps = Maps.uniqueIndex(arrayList, (Function<V, String>) v -> (String) v.getValue());
 
             this.values = maps;
 
 
 
             //  this.values =  collection.stream().collect(HashMap::new,(m,v)->m.put((ConfigEntry)v.getValue(),v),HashMap::putAll);
             System.out.println(this.values.size() + "read");
 
         } catch (Exception e) {
             e.printStackTrace();
         }
 
 
     }
 */
    public V getEntry(K obj) {
        return (V) (this.values.get(this.getObjectKey(obj)));
    }

    public void addEntry(V entry) {
        this.values.put(this.getObjectKey(entry.getValue()), entry);

        try {
            this.writeChanges();
        } catch (Exception e) {
            System.out.println("server config write fail");
        }

    }

    protected ServerConfigEntry<K> createEntry(JsonObject entryData) {
        return new ServerConfigEntry<>(null, entryData);
    }

    protected String getObjectKey(K obj) {
        return obj.toString();
    }

    protected boolean hasEntry(K entry) {
        return this.values.containsKey(this.getObjectKey(entry));
    }

    public String[] getKeys() {
        System.out.println(this.getValues().size());
        String[] astring = new String[this.getValues().size()];
        int i = 0;

        for (V serverConfigEntry : this.getValues().values()) {
            astring[i++] = (String) ((ServerConfigEntry) serverConfigEntry).getValue();
        }

        return astring;
    }

    protected Map<String, V> getValues() {
        return this.values;
    }


    public void writeChanges() throws IOException {

        Collection<V> collection = this.values.values();
        String s = this.gson.toJson(collection);
        BufferedWriter bufferedwriter = null;

        try {
            bufferedwriter = Files.newWriter(this.saveFile, StandardCharsets.UTF_8);
            bufferedwriter.write(s);
        } finally {
            if (bufferedwriter != null) {
                bufferedwriter.close();
            }
        }
    }


    protected void onSerialization(JsonObject data) {
    }


    class Serializer implements JsonDeserializer<ServerConfigEntry<K>>, JsonSerializer<ServerConfigEntry<K>> {
        private Serializer() {
        }

        public JsonElement serialize(ServerConfigEntry<K> p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
            JsonObject jsonobject = new JsonObject();
            p_serialize_1_.onSerialization(jsonobject);
            return jsonobject;
        }

        public ServerConfigEntry<K> deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            if (p_deserialize_1_.isJsonObject()) {
                JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
                return ServerConfig.this.createEntry(jsonobject);
            } else {
                return null;
            }
        }
    }

}
