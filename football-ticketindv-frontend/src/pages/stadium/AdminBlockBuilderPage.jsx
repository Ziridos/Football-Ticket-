import React, { useEffect, useState, useCallback } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import AdminBlockBuilder from '../../components/stadium/AdminBlockBuilder';
import stadiumApi from '../../services/stadiumApi';
import Layout from '../../components/layout/Layout';
import StadiumBuilderStepper from '../../components/stadium/StadiumBuilderStepper';
import InfoTooltip from '../../components/common/InfoTooltip';
import BuilderSuccessNotification from '../../components/common/BuilderSuccessNotification';

const AdminBlockBuilderPage = () => {
    const { id: boxId } = useParams();
    const location = useLocation();
    const navigate = useNavigate();
    const { isAuthenticated } = useAuth();
    const [stadiumId, setStadiumId] = useState(null);
    const [existingBlocks, setExistingBlocks] = useState([]);
    const [error, setError] = useState(null);
    const [showSuccess, setShowSuccess] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        if (!isAuthenticated) {
            navigate('/login');
            return;
        }
        const { stadiumId: passedStadiumId } = location.state || {};
        setStadiumId(passedStadiumId);
    }, [location, boxId, isAuthenticated, navigate]);

    const fetchBlocks = useCallback(async () => {
        if (!stadiumId || !boxId) return;

        try {
            const blocks = await stadiumApi.getBoxBlocks(stadiumId, boxId);
            setExistingBlocks(blocks);
            setError(null);
        } catch (error) {

            const errorMessage = typeof error.response?.data === 'string' 
                ? error.response?.data 
                : error.response?.data?.message;

            setError(errorMessage || 'Failed to load blocks. Please try again later.');
            if (error.message.includes('Please login')) {
                navigate('/login');
            }
        }
    }, [stadiumId, boxId, navigate]);

    useEffect(() => {
        if (stadiumId && boxId && isAuthenticated) {
            fetchBlocks();
        }
    }, [fetchBlocks, stadiumId, boxId, isAuthenticated]);

    //delete
    const handleDeleteBlock = async (blockId) => {
        if (!stadiumId || !boxId) return;

        try {
            await stadiumApi.deleteBlock(stadiumId, boxId, blockId);
            setExistingBlocks(prevBlocks => prevBlocks.filter(block => block.blockId !== blockId));
            setSuccessMessage('Block deleted successfully!');
            setShowSuccess(true);
        } catch (error) {

            const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

            if (error.message.includes('Please login')) {
                navigate('/login');
            } else {
                setError(errorMessage || 'Failed to delete block. Please try again.');
            }
            console.error('Error deleting block:', error);
        }
    };

    const handleSaveBlock = async (block) => {
        if (!stadiumId || !boxId) return;
    
        try {
            const blockToSave = {
                ...block,
                xposition: block.x,
                yposition: block.y,
                seats: block.seats.map(seat => ({
                    ...seat,
                    seatNumber: seat.seatId.toString()
                }))
            };
    
            const newBlock = await stadiumApi.saveBlock(stadiumId, boxId, blockToSave);
    
            setExistingBlocks(prevBlocks => {
                if (!prevBlocks.some(b => b.blockId === newBlock.blockId)) {
                    return [...prevBlocks, {
                        ...newBlock,
                        x: newBlock.xposition,
                        y: newBlock.yposition,
                        xPosition: newBlock.xposition,
                        yPosition: newBlock.yposition,
                        seats: newBlock.seats.map(seat => ({
                            ...seat,
                            x: seat.xposition,
                            y: seat.yposition
                        }))
                    }];
                }
                return prevBlocks.map(b => 
                    b.blockId === newBlock.blockId 
                        ? {
                            ...newBlock,
                            x: newBlock.xposition,
                            y: newBlock.yposition,
                            xPosition: newBlock.xposition,
                            yPosition: newBlock.yposition,
                            seats: newBlock.seats.map(seat => ({
                                ...seat,
                                x: seat.xposition,
                                y: seat.yposition
                            }))
                          }
                        : b
                );
            });
            setError(null);
            setSuccessMessage('Block created successfully!');
            setShowSuccess(true);
        } catch (error) {

            const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

            if (error.message.includes('Please login')) {
                navigate('/login');
            } else if (error.message === 'Block overlap') {
                setError(errorMessage || 'Blocks cannot overlap. Please try again with a different position.');
            } else {
                setError(errorMessage || 'Failed to save block. Please try again.');
            }
        }
    };

    if (!isAuthenticated || !stadiumId || !boxId) {
        return null;
    }

    return (
        <Layout>
            <div className="container-fluid px-4 py-4 overflow-y-scroll" style={{ height: 'calc(100vh - 60px)' }}>
                <BuilderSuccessNotification 
                    message={successMessage}
                    isVisible={showSuccess}
                    onClose={() => setShowSuccess(false)}
                />
                <StadiumBuilderStepper currentStep={2} />
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h2 className="fs-2 fw-bold mb-1">Box Block Builder</h2>
                        <div style={{ position: 'absolute', zIndex: 9999, top: '100px', left: '20px' }}>
                        <InfoTooltip 
    content="Create blocks within your box to organize seating. Seats will be automatically generated within each block based on your configuration. Click and drag to create a block - the size will determine how many seats are generated. Block names should be descriptive (e.g., Block A1). You can only draw one block at a time and blocks cannot overlap."
/>
                        </div>
                        <p className="text-muted mb-0">
                            Stadium ID: {stadiumId} | Box ID: {boxId}
                        </p>
                    </div>
                </div>

                {error && (
                    <div className="alert alert-danger mb-4" role="alert">
                        {error}
                    </div>
                )}

                <div className="card shadow-sm">
                    <div className="card-body">
                        <AdminBlockBuilder 
                            stadiumId={stadiumId}
                            boxId={boxId}
                            existingBlocks={existingBlocks}
                            onSaveBlock={handleSaveBlock}
                            onDeleteBlock={handleDeleteBlock}
                        />
                    </div>
                </div>
            </div>
        </Layout>
    );
};

export default AdminBlockBuilderPage;