import React from 'react';
import { Info } from 'lucide-react';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "../common/tooltip";

const InfoTooltip = ({ content }) => {
  return (
    <TooltipProvider>
      <Tooltip>
        <TooltipTrigger className="border-0 bg-transparent p-0">
          <Info className="text-muted ms-2" size={18} />
        </TooltipTrigger>
        <TooltipContent>
          <div className="bg-dark text-white rounded p-2" style={{ maxWidth: "200px" }}>
            {content}
          </div>
        </TooltipContent>
      </Tooltip>
    </TooltipProvider>
  );
};

export default InfoTooltip;