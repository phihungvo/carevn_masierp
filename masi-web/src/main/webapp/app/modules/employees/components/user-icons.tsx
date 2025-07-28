
import React from 'react';
interface SvgIconProps {
  width?: number;
  height?: number;
  fill?: string;
  className?: string;
}
export function UserCheckIcon({ width = 20, height = 20, fill = "currentColor", className }: SvgIconProps) {
  const classes = className ? `bi bi-person-check ${className}` : "bi bi-person-check";
  return (
    <svg xmlns="http://www.w3.org/2000/svg" width={width} height={height} fill={fill} className={classes}
      viewBox="0 0 16 16">
      <path d="M12.5 16a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7m1.679-4.493-1.335 2.226a.75.75 0 0 1-1.174.144l-.774-.773a.5.5 0 0 1 .708-.708l.547.548 1.17-1.951a.5.5 0 1 1 .858.514M11 5a3 3 0 1 1-6 0 3 3 0 0 1 6 0M8 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4" />
      <path d="M8.256 14a4.5 4.5 0 0 1-.229-1.004H3c.001-.246.154-.986.832-1.664C4.484 10.68 5.711 10 8 10q.39 0 .74.025c.226-.341.496-.65.804-.918Q8.844 9.002 8 9c-5 0-6 3-6 4s1 1 1 1z" />
    </svg>
  )
}

export function UserUpdateIcon({ width = 16, height = 16, fill = "currentColor", className }: SvgIconProps) {
  const classes = className ? `bi bi-person-fill-add pointer ${className}` : "bi bi-person-fill-add pointer";
  return (
    <svg xmlns="http://www.w3.org/2000/svg" width={width} height={height} fill={fill} className={classes}
      viewBox="0 0 16 16">
      <path d="M12.5 16a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7m.5-5v1h1a.5.5 0 0 1 0 1h-1v1a.5.5 0 0 1-1 0v-1h-1a.5.5 0 0 1 0-1h1v-1a.5.5 0 0 1 1 0m-2-6a3 3 0 1 1-6 0 3 3 0 0 1 6 0" />
      <path d="M2 13c0 1 1 1 1 1h5.256A4.5 4.5 0 0 1 8 12.5a4.5 4.5 0 0 1 1.544-3.393Q8.844 9.002 8 9c-5 0-6 3-6 4" />
    </svg>
  )
}
