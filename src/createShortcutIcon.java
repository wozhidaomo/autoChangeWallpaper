import net.jimmc.jshortcut.JShellLink;

import java.io.File;
import java.lang.reflect.Field;

public class createShortcutIcon {
    /**
     * 创建一个快捷方式
     *
     * @param fileOrFolderPath
     *            源文件夹路径
     * @param writeShortCutPath
     *            目标文件路径(快捷方式型)
     */
    public static void createShortCut(String fileOrFolderPath,String writeShortCutPath) {
        try{
            addLibraryDir(new File(".").getCanonicalPath()+"\\libraryPath");
            System.out.println(System.getProperty("java.library.path"));
            JShellLink link = new JShellLink();
            writeShortCutPath.replaceAll("/", "\\");
            String folder = writeShortCutPath.substring(0, writeShortCutPath.lastIndexOf("\\"));
            String name = writeShortCutPath.substring(writeShortCutPath.lastIndexOf("\\") + 1, writeShortCutPath.length());
            //System.out.println(fileOrFolderPath);
            link.setName(name);// 目的快捷方式文件夹名称
            link.setFolder(folder);// 目的快捷方式文件路径片段
            link.setPath(fileOrFolderPath);
            link.save();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 获取一个快捷方式真实地址
     *
     * @param fileFolderPath
     *            源文件夹路径
     */
    public static String getShortCutRealPath(String fileFolderPath) {
        // 根据快捷方式的路径和文件夹名,获取源文件夹地址
        fileFolderPath.replaceAll("/", "\\");
        String folder = fileFolderPath.substring(0, fileFolderPath.lastIndexOf("\\"));
        String name = fileFolderPath.substring(fileFolderPath.lastIndexOf("\\") + 1, fileFolderPath.length());
        JShellLink link = new JShellLink(folder, name);
        link.load();
        //System.out.println(link.getPath());
        return link.getPath();
    }

    private static void addLibraryDir(String libraryPath) throws Exception {
        Field userPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        userPathsField.setAccessible(true);
        String[] paths = (String[]) userPathsField.get(null);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            if (libraryPath.equals(paths[i])) {
                continue;
            }
            sb.append(paths[i]).append(';');
        }
        sb.append(libraryPath);
        System.setProperty("java.library.path", sb.toString());
        final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
        sysPathsField.setAccessible(true);
        sysPathsField.set(null, null);
    }

    public static void main(String args[]) {
        try{

//            System.setProperty("java.library.path",new File(".").getCanonicalPath()+"\\libraryPath");
            System.out.println(System.getProperty("java.library.path"));
            String fileFolderPath = new File(".").getCanonicalPath()+"\\autoStart.bat";//文件存放路径
            String writeFolderPath =  new File(".").getCanonicalPath()+"\\abc\\autoStart.bat"; //写入路径
            System.out.println("fileFolder:"+fileFolderPath+"     writeFolder"+writeFolderPath);
            createShortCut(fileFolderPath, writeFolderPath);

        }catch (Exception e){
            e.printStackTrace();

        }

        //String path=getShortCutRealPath(writeFolderPath);
        //System.out.println(path);
    }
}
