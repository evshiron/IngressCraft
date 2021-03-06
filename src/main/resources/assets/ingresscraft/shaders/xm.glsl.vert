// GENERATED FILE //
#ifndef GL_ES
#define lowp
#define mediump
#define highp
#endif
attribute vec2 a_texCoord0;
attribute vec3 a_position;
uniform float u_elapsedTime;
uniform mat4 u_modelViewProject;
varying vec4 v_texCoord0And1;
void main ()
{
  vec4 tmpvar_1;
  tmpvar_1.xy = gl_MultiTexCoord0.xy;
  tmpvar_1.zw = (gl_MultiTexCoord0.xy * 1.35);
  vec4 tmpvar_2;
  tmpvar_2.x = 0.0;
  tmpvar_2.y = (u_elapsedTime * 0.6);
  tmpvar_2.z = (u_elapsedTime * 0.6);
  tmpvar_2.w = (u_elapsedTime * 0.45);
  v_texCoord0And1 = (tmpvar_1 + tmpvar_2);
  vec4 tmpvar_3;
  tmpvar_3.w = 1.0;
  tmpvar_3.xyz = gl_Vertex.xyz;
  gl_Position = (gl_ModelViewProjectionMatrix * tmpvar_3);
}

