package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glCreateProgram;

public class Shader
{
    int shaderProgram;
    Shader() throws IOException
    {

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexShader, load("src/main/resources/vertex.glsl"));

        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentShader, load("src/main/resources/fragment.glsl"));

        glCompileShader(fragmentShader);

        this.shaderProgram = glCreateProgram();

        glAttachShader(this.shaderProgram, vertexShader);

        glAttachShader(this.shaderProgram, fragmentShader);

        glLinkProgram(this.shaderProgram);

        glDeleteShader(vertexShader);

        glDeleteShader(fragmentShader);
    }


    public static String load(String path) throws IOException
    {
        return Files.readString(Path.of(path));
    }

    public void uploadTexture(String name)
    {
        int location = glGetUniformLocation(this.shaderProgram, name);
        glUseProgram(this.shaderProgram);
        glGetUniformi(location, 0);
    }

}

