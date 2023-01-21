package org.example;

import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.IOException;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    static Shader s;

    public static void main(String[] args) throws IOException
    {
        long window;
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(800, 800, "Hello world", NULL, NULL);

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glfwSwapInterval(1);
        glfwShowWindow(window);


        s = new Shader();



        float[] vertices =
        {
            0.0f, 0.0f, 0.0f,     1.0f, 0.0f, 0.0f,       0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,      0.0f, 1.0f, 0.0f,       0.0f, 1.0f,
            1.0f, 1.0f, 0.0f,       0.0f, 0.0f, 1.0f,       1.0f, 1.0f,
            1.0f, 0.0f, 0.0f,      1.0f, 1.0f, 0.0f,       1.0f, 0.0f
        };

        int[] indices =
        {
            0, 1, 2,
            0, 3, 2
        };




        FloatBuffer vertice = BufferUtils.createFloatBuffer(vertices.length);
        vertice.put(vertices).flip();

        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices).flip();

        int vao = glGenVertexArrays();
        int vbo = glGenBuffers();
        int ebo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertice, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8*Float.BYTES, 3*Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8*Float.BYTES, 6*Float.BYTES);
        glEnableVertexAttribArray(2);

        glBindVertexArray(vao);


        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer texels = stbi_load("src/main/resources/images/cat.png", width, height, channels, 0);

        int texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, texels);
        glGenerateMipmap(texture);

        stbi_image_free(texels);
        glBindTexture(GL_TEXTURE_2D, texture);

        s.uploadTexture("tex0");


        while(!glfwWindowShouldClose(window))
        {
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);
            glUseProgram(s.shaderProgram);

            Matrix4f model = new Matrix4f(1.0f);

            glBindTexture(GL_TEXTURE_2D, texture);
//            glDrawArrays(GL_TRIANGLES, 0, 3);
            glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        glfwTerminate();

    }


    public static void uploadFloat(String name, float f)
    {
        int location = glGetUniformLocation(s.shaderProgram, name);
        glUseProgram(s.shaderProgram);
        glUniform1f(location, f);
    }



}
