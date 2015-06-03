// GENERATED FILE //
#version 110
#ifndef GL_ES
#define lowp
#define mediump
#define highp
#endif
attribute vec2 a_texCoord0;
attribute vec3 a_position;
uniform mat4 u_modelViewProject;
varying vec2 v_texCoord0;
void main ()
{
  v_texCoord0 = gl_MultiTexCoord0.xy;
  vec4 tmpvar_1;
  tmpvar_1.w = 1.0;
  tmpvar_1.xyz = gl_Vertex.xyz;
  gl_Position = (gl_ModelViewProjectionMatrix * tmpvar_1);
}

