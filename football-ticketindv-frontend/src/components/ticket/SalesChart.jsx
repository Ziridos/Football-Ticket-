import React from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend
} from 'recharts';

const SalesChart = ({ data, type }) => {
  const formatYAxis = (value) => `€${value}`;
  
  return (
    <div className="mt-4" style={{ width: '100%', height: 400 }}>
      <ResponsiveContainer>
        <LineChart
          data={data}
          margin={{
            top: 20,
            right: 30,
            left: 60,
            bottom: 5,
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis 
            dataKey="label" 
            stroke="#666"
          />
          <YAxis 
            tickFormatter={formatYAxis}
            stroke="#666"
          />
          <Tooltip 
            formatter={(value) => [`€${value}`, 'Revenue']}
            contentStyle={{ 
              backgroundColor: '#333',
              border: 'none',
              borderRadius: '4px',
              color: '#fff'
            }}
          />
          <Legend />
          <Line 
            type="monotone"
            dataKey="revenue" 
            stroke="#dc3545"
            strokeWidth={2}
            dot={{ 
              fill: '#dc3545',
              r: 4
            }}
            activeDot={{ 
              fill: '#dc3545',
              r: 6,
              stroke: '#fff',
              strokeWidth: 2
            }}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default SalesChart;