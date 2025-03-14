
import React from 'react';
import { Link } from 'react-router-dom';

const BoxPricingRuleList = ({ rules, stadiumId, onDelete }) => {
  return (
    <div>
      <h3>Box Pricing Rules</h3>
      <Link to={`/create-box-pricing-rule/${stadiumId}`} className="btn btn-primary mb-3">Create New Rule</Link>
      {rules.map((rule) => (
        <div key={rule.id} className="card mb-3">
          <div className="card-body">
            <h5 className="card-title">Rule ID: {rule.id}</h5>
            <p>Occupancy Threshold: {rule.occupancyThreshold}</p>
            <p>Price Increase: {rule.priceIncreasePercentage}%</p>
            <div className="d-flex justify-content-between">
              <Link to={`/edit-box-pricing-rule/${stadiumId}/${rule.id}`} className="btn btn-warning">Edit</Link> 
              <button onClick={() => onDelete(rule.id)} className="btn btn-danger">Delete</button>
            </div>
          </div> 
        </div>
      ))}
    </div>
  );
};

export default BoxPricingRuleList;