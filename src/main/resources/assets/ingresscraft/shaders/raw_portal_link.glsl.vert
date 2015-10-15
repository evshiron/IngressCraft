uniform mat4 u_modelViewProject;
uniform vec3 u_cameraFwd;
uniform float u_elapsedTime;
attribute vec4 a_position;
attribute vec4 a_texCoord0;
attribute vec4 a_color;
varying vec4 v_texCoord0And1;
varying vec4 v_color;
void main() {
  vec3 normal = normalize(vec3(gl_MultiTexCoord0.z, .001, gl_MultiTexCoord0.w));
  v_texCoord0And1.xy = (gl_MultiTexCoord0.xy + vec2(0, u_elapsedTime * gl_Vertex.w * 0.6));
  v_texCoord0And1.zw = gl_MultiTexCoord0.xy + vec2(0, u_elapsedTime * gl_Vertex.w);
  v_color = gl_Color;
  float alpha = abs(dot(normal, u_cameraFwd));
  v_color.a *= (3.0 * alpha * alpha) - (2.0 * alpha * alpha * alpha);
  gl_Position = gl_ModelViewProjectionMatrix * vec4(gl_Vertex.xyz, 1.0);
}
