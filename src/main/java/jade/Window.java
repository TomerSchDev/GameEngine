package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwwindow;
    private static Window window = null;
    private float r, g, b, a;
    private boolean fadeToDark=false;
    private void fadeToDark() {
        if(fadeToDark){
            r=Math.max(r-0.01f,0);
            g=Math.max(g-0.01f,0);;
            b=Math.max(b-0.01f,0);;
            a=Math.max(a-0.01f,0);;
        }
    }

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "mario";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        init();
        loop();
        //free the memory
        glfwFreeCallbacks(glfwwindow);
        glfwDestroyWindow(glfwwindow);

        //Terminaite GLFW and free the memory
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        //set up error callback
        GLFWErrorCallback.createPrint(System.err).set();
        //Init GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("unable to install GLFW. ");

        }
        //confirm
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        //create the window
        glfwwindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwwindow == NULL) {
            throw new IllegalStateException("Faild to create the GLFW window");
        }
        glfwSetCursorPosCallback(glfwwindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwwindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwwindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwwindow, KeyListener::KeyCallBack);
        //make the openGL context current
        glfwMakeContextCurrent(glfwwindow);
        //enable v-synce
        glfwSwapInterval(1);
        //make the window visable
        glfwShowWindow(glfwwindow);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

    }

    public void loop() {
        float beginTime= Time.getTime();
        float endTime=Time.getTime();
        while (!glfwWindowShouldClose(glfwwindow)) {
            //Poll evevnts
            glfwPollEvents();
            glClearColor(r, g, b, a);
            fadeToDark();
            glClear(GL_COLOR_BUFFER_BIT);
            if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
                fadeToDark=true;
            }
            glfwSwapBuffers(glfwwindow);
            endTime=Time.getTime();
            float dt=endTime-beginTime;
            beginTime=endTime;
        }
    }


}