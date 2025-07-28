import { Chart } from 'chart.js/auto';
import { CategoryScale } from 'chart.js';
import React from 'react';
import { Pie } from 'react-chartjs-2';

Chart.register(CategoryScale);

interface PieChartProps {
  chartData: any;
  text?: string;
  width?: number;
  height?: number;
}

const PieChart = (props: PieChartProps) => {
  const { chartData, text, width, height } = props;

  return (
    <Pie
      data={chartData}
      options={{
        plugins: {
          title: {
            display: true,
            text,
          },
        },
        responsive: true,
        maintainAspectRatio: false,
      }}
      width={width}
      height={height}
    />
  );
};

export default PieChart;
