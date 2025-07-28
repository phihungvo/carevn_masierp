import { Chart } from 'chart.js/auto';
import { CategoryScale } from 'chart.js';
import React from 'react';
import { Bar } from 'react-chartjs-2';

Chart.register(CategoryScale);

interface BarChartProps {
  title?: string;
  chartData: any;
}

const BarChart = (props: BarChartProps) => {
  const { chartData, title } = props;

  return (
    <Bar
      data={chartData}
      options={{
        plugins: {
          title: {
            display: true,
            text: title,
          },
          legend: {
            display: true,
          },
        },
        responsive: true,
        maintainAspectRatio: true,
      }}
    />
  );
};

export default BarChart;
