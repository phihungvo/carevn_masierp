import React, { useState } from 'react';
import './time-sheet.scss'; // Ensure you have this CSS file

interface SvgIconProps {
  width?: number;
  height?: number;
  fill?: string;
  className?: string;
}

export function RefreshIcon({ width = 24, height = 24, fill = "currentColor", className }: SvgIconProps) {
  const [isRotating, setIsRotating] = useState(false);
  const [isClickable, setIsClickable] = useState(true);

  const handleClick = () => {
    if (!isClickable) return;

    setIsRotating(true);
    setIsClickable(false);
    setTimeout(() => {
      setIsRotating(false);
      setIsClickable(true);
    }, 1000); // Reset rotation after 1 second
  };

  const classes = className ? `bi bi-arrow-clockwise ${className}` : "bi bi-arrow-clockwise";
  const combinedClasses = `${classes} ${isRotating ? 'rotate' : ''}`;

  return (
    <svg xmlns="http://www.w3.org/2000/svg"
      width={width} height={height}
      fill={fill}
      className={combinedClasses}
      viewBox="0 0 16 16"
      onClick={handleClick}>
      <path fillRule="evenodd" d="M8 3a5 5 0 1 0 4.546 2.914.5.5 0 0 1 .908-.417A6 6 0 1 1 8 2z" />
      <path d="M8 4.466V.534a.25.25 0 0 1 .41-.192l2.36 1.966c.12.1.12.284 0 .384L8.41 4.658A.25.25 0 0 1 8 4.466" />
    </svg>
  );
}
