import React, { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import AdminBoxBuilder from '../../components/box/AdminBoxBuilder';
import boxApi from '../../services/boxApi';
import Layout from '../../components/layout/Layout';
import StadiumBuilderStepper from '../../components/stadium/StadiumBuilderStepper';
import InfoTooltip from '../../components/common/InfoTooltip';
import BuilderSuccessNotification from '../../components/common/BuilderSuccessNotification';

const AdminBoxBuilderPage = () => {
    const { id: stadiumId } = useParams();
    const navigate = useNavigate();
    const { user: authUser } = useAuth();
    const [existingBoxes, setExistingBoxes] = useState([]);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [showSuccess, setShowSuccess] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        if (authUser?.role !== 'ADMIN') {
            navigate('/');
            return;
        }
    }, [authUser, navigate]);

    const fetchBoxes = useCallback(async () => {
        try {
            setIsLoading(true);
            setError(null);
            const formattedBoxes = await boxApi.getBoxesByStadium(stadiumId);
            setExistingBoxes(formattedBoxes);
        } catch (error) {

            const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

            setError(errorMessage || 'Failed to load boxes. Please try again later.');
            if (error.response?.status === 401) {
                navigate('/login');
            }
            console.error('Error fetching boxes:', error);
        } finally {
            setIsLoading(false);
        }
    }, [stadiumId, navigate]);

    useEffect(() => {
        if (authUser?.role === 'ADMIN') {
            fetchBoxes();
        }
    }, [fetchBoxes, authUser]);
    
    const handleSaveBox = async (box) => {
        const boxToSave = {
            ...box,
            xposition: box.x,
            yposition: box.y,
            price: box.price,
        };

        try {
            setError(null);
            const newBox = await boxApi.createBox(stadiumId, boxToSave);
            setExistingBoxes(prevBoxes => {
                if (!prevBoxes.some(b => b.boxId === newBox.boxId)) {
                    return [...prevBoxes, {
                        ...newBox,
                        x: newBox.xposition,
                        y: newBox.yposition,
                        xPosition: newBox.xposition,
                        yPosition: newBox.yposition,
                        price: newBox.price,
                    }];
                }
                return prevBoxes;
            });
            setSuccessMessage('Box created successfully!');
            setShowSuccess(true);
        } catch (error) {

            const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

            if (error.response?.status === 401) {
                navigate('/login');
            } else if (error === 'Box overlap') {
                setError(errorMessage || 'Boxes cannot overlap. Please try again with a different position.');
            } else {
                setError(errorMessage| 'Failed to save box. Please try again.');
            }
            console.error('Error saving box:', error);
        }
    };

    const handleDeleteBox = async (boxId) => {
        try {
            setError(null);
            await boxApi.deleteBox(stadiumId, boxId);
            setExistingBoxes(prevBoxes => prevBoxes.filter(box => box.boxId !== boxId));
            setSuccessMessage('Box deleted successfully!');
            setShowSuccess(true);
        } catch (error) {

            const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

            if (error.response?.status === 401) {
                navigate('/login');
            }
            setError(errorMessage || 'Failed to delete box. Please try again.');
            console.error('Error deleting box:', error);
        }
    };

    const handleUpdateBoxPrice = async (boxId, newPrice) => {
        try {
            setError(null);
            const updatedBox = await boxApi.updateBoxPrice(stadiumId, boxId, newPrice);
            setExistingBoxes(prevBoxes => 
                prevBoxes.map(box => 
                    box.boxId === updatedBox.boxId ? { ...box, price: updatedBox.updatedPrice } : box
                )
            );
            setSuccessMessage('Box price updated successfully!');
            setShowSuccess(true);
        } catch (error) {

            const errorMessage = typeof error.response?.data === 'string' 
        ? error.response?.data 
        : error.response?.data?.message;

            if (error.response?.status === 401) {
                navigate('/login');
            }
            setError(errorMessage || 'Failed to update box price. Please try again.');
            console.error('Error updating box price:', error);
        }
    };

    if (authUser?.role !== 'ADMIN') return null;

    return (
        <Layout>
            <div className="container-fluid px-4 py-4 overflow-y-scroll" style={{ height: 'calc(100vh - 60px)' }}>
                <BuilderSuccessNotification 
                    message={successMessage}
                    isVisible={showSuccess}
                    onClose={() => setShowSuccess(false)}
                />
                <StadiumBuilderStepper currentStep={1} />
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h2 className="fs-2 fw-bold mb-1">Box Builder</h2>
                        <p className="text-muted mb-0">Stadium ID: {stadiumId}</p>
                        <div style={{ position: 'absolute', zIndex: 9999, top: '100px', left: '20px' }}>
                        <InfoTooltip 
    content="Click and drag on the layout to create boxes. Each box represents a section that can contain multiple blocks and seats. You can set prices for each box which will be inherited by all seats within it. To set a price for a box, hold the box with LMB and an update menu will appear. Box names should be descriptive (e.g., VIP Box 1) and prices should be positive numbers (e.g., 100.00)."
/>
                        </div>
                    </div>
                    <Link to="/stadiums" className="btn btn-outline-secondary">
                        Back to Stadiums
                    </Link>
                </div>

                {error && (
                    <div className="alert alert-danger mb-4" role="alert">
                        {error}
                    </div>
                )}

                {isLoading ? (
                    <div className="d-flex justify-content-center py-5">
                        <div className="spinner-border text-danger" role="status">
                            <span className="visually-hidden">Loading...</span>
                        </div>
                    </div>
                ) : (
                    <div className="card shadow-sm">
                        <div className="card-body">
                            <AdminBoxBuilder 
                                stadiumId={stadiumId} 
                                existingBoxes={existingBoxes} 
                                onSaveBox={handleSaveBox} 
                                onUpdateBoxPrice={handleUpdateBoxPrice}
                                onDeleteBox={handleDeleteBox}
                            />
                        </div>
                    </div>
                )}
            </div>
        </Layout>
    );
};

export default AdminBoxBuilderPage;