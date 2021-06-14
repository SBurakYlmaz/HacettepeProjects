pragma solidity >=0.7.0 <0.8.0;

contract TaxiBusiness{
    
    //------------------------------------------------Structs Start --------------------------------------------------------

    
    struct Participant {
        address payable participantAddress;
		uint participantAccountBalance;
    }
    
    struct TaxiDriver {
        address payable taxiDriverAddress;
		uint taxiDriverAccountBalance; 
		uint salary;
		uint8 approvalCount;
        uint8 approvalState;
    }
    
    struct ProposedCar {
        uint32 proposedCarID;
        uint price;
        uint offerValidTime;
        uint8 approvalCount;
        uint8 approvalState;
    }
    
    //------------------------------------------------Structs End --------------------------------------------------------
 
 
    //------------------------------------------------State Variables Start --------------------------------------------------------

    address  manager;
    address payable carDealer;
    
    address[] public participantsArray; 
    TaxiDriver public driver;
    TaxiDriver  possibleDriver;
    ProposedCar  proposedCar; 
    ProposedCar  repurchasedPropesedCar; 
    
    uint256 public contractBalance;
    uint  driverLastPayDate;
    uint  participantFee;
    uint  participantCount;
    uint  fixExpenses;
    uint  lastCarExpenseTime;
    uint  lastDividedTime;
    uint32 public ownedCarId;

    mapping(address => bool)  alreadyApprovedParticipants; //This mapping is for not allowing double approving for the ProposedCar
    mapping(address => bool)  alreadyApprovedParticipantsRepurchased; //This mapping is for not allowing double approving for the repurchasedProposedCar
    mapping(address => bool)  taxiDriverHireApprove; //This mapping is for not allowing double approving for the hireDriver

    mapping(address => Participant) public participantMap;


    //------------------------------------------------State Variables End --------------------------------------------------------


    //------------------------------------------------Modifiers Start ------------------------------------------------------------

    
    modifier onlyManager {
        require(msg.sender == manager, "Only manager can call this function.");
        _;
    }
    
    modifier onlyCarDealer {
        require(msg.sender == carDealer, "Only car dealer can call this function.");
        _;
    }
    
     modifier onlyParticipant {
        require(participantMap[msg.sender].participantAddress == msg.sender, "Only participants can call this function.");
        _;
    }
    
     modifier onlyDriver(){
        require(msg.sender == driver.taxiDriverAddress, "Only driver can call this function");
        _;
    }
    //------------------------------------------------Modifiers End --------------------------------------------------------------

    
    constructor() {
        manager = msg.sender;
        contractBalance = 0;
        participantFee = 10 ether;
        participantCount = 9;
        fixExpenses = 10;
    }
    
    //------------------------------------------------Getters Start --------------------------------------------------------------

    
    function getContractBalance() public view returns(uint){
        return address(this).balance;
    }
    
    //------------------------------------------------Getters End --------------------------------------------------------------

    
    //------------------------------------------------Internal Functions Start --------------------------------------------------------
	
	//These functions general purpose is resetting the votes in each election.For example if a driver proposed and some of the participants approved,
	//however suddenly, instead of that driver another driver proposed we need to reset all the votes.For this purpose I used below functions.
    
	function resetApprovePurchaseMap() internal{
       for ( uint i = 0 ; i < participantsArray.length ; i ++){
               alreadyApprovedParticipants[participantsArray[i]] = false;
            }
    }
    
    function resetApproveRePurchaseMap() internal{
       for ( uint i = 0 ; i < participantsArray.length ; i ++){
               alreadyApprovedParticipantsRepurchased[participantsArray[i]] = false;
            }
    }
    
    function resetTaxiDriverHire() internal{
       for ( uint i = 0 ; i < participantsArray.length ; i ++){
               taxiDriverHireApprove[participantsArray[i]] = false;
            }
    }
    
    //------------------------------------------------Internal Functions End --------------------------------------------------------
    
    //------------------------------------------------External Functions Start --------------------------------------------------------
    
    function join() external payable {
        require(participantsArray.length < participantCount, 'No more than 9 participants allowed!');
        require(msg.value == participantFee, 'Joining fee must be 10 ether!');
        require(participantMap[msg.sender].participantAddress != msg.sender, 'This account already participated into the contract');
        
        
        contractBalance = contractBalance + participantFee;

        Participant memory participant = Participant({participantAddress: msg.sender, participantAccountBalance:0 ether});
        participantsArray.push(msg.sender);
        participantMap[msg.sender] = participant;
      
    }
    
    function setCarDealer(address payable carDealerAddress) external onlyManager() {
        carDealer = carDealerAddress;
	}
	
	function carProposeToBusiness(uint32 _proposedCarId, uint _price, uint _offerValidTime) external onlyCarDealer() {
        ProposedCar memory currentPropesedCar = ProposedCar({
            proposedCarID: _proposedCarId,
            price: _price,
            offerValidTime: _offerValidTime,
            approvalCount: 0,
            approvalState : 0
        });
        proposedCar = currentPropesedCar;
        resetApprovePurchaseMap();
    }
    
    function approvePurchaseCar() external onlyParticipant() {
        require(!alreadyApprovedParticipants[msg.sender]==true, 'This participant already approved for this car.');

        alreadyApprovedParticipants[msg.sender] = true;
        proposedCar.approvalCount++;
    }
    
    function purchaseCar() external  onlyManager() {
        require(proposedCar.approvalCount > participantsArray.length / 2, "Not enought approved votes to buy this car");
        require(getContractBalance() >= proposedCar.price, "Not enough balance in contract");
        require(block.timestamp < proposedCar.offerValidTime, 'Offered valid time already exceeded.');


        ownedCarId = proposedCar.proposedCarID;
        proposedCar.approvalState = 1;
        contractBalance = contractBalance - proposedCar.price;
        proposedCar.offerValidTime = 0;

        carDealer.transfer(proposedCar.price);
        
    }
    
    function repurchaseCarPropose(uint32 _proposedCarId, uint _price, uint _offerValidTime) external onlyCarDealer() {
        ProposedCar memory currentPropesedCar = ProposedCar({
            proposedCarID: _proposedCarId,
            price: _price,
            offerValidTime: _offerValidTime,
            approvalCount: 0,
            approvalState : 0
        });
        repurchasedPropesedCar = currentPropesedCar;
        resetApproveRePurchaseMap();//Resetting if new car is proposed
    }
    
    function approveSellProposal() external onlyParticipant() {
        require(!alreadyApprovedParticipantsRepurchased[msg.sender]==true, 'This participant already approved for this car.');

        alreadyApprovedParticipantsRepurchased[msg.sender] = true;
        repurchasedPropesedCar.approvalCount++;
    }
    
    function repurchaseCar() public payable onlyCarDealer() {
	    require(msg.value == repurchasedPropesedCar.price, "Price is not equivalent to car price.");
        require(repurchasedPropesedCar.approvalCount > participantsArray.length / 2, "Not enought approved votes to sell this car");
        require(carDealer.balance >= repurchasedPropesedCar.price, "Car Dealer balance is not enough");
        require(block.timestamp < repurchasedPropesedCar.offerValidTime, 'Offered valid time already exceeded.');

        ownedCarId = 0;
        
        contractBalance = contractBalance + repurchasedPropesedCar.price;

        delete repurchasedPropesedCar;
	}
	
	function proposeDriver(address payable driverAddress, uint salary) external onlyManager() {
	    TaxiDriver memory newDriver = TaxiDriver({
    	    taxiDriverAddress : driverAddress,
    	    taxiDriverAccountBalance : 0,
    	    salary : salary,
		    approvalCount : 0,
		    approvalState : 0
    	});
    	possibleDriver = newDriver;
        resetTaxiDriverHire();
	}
    
    function approveDriver() external onlyParticipant() {
	    require(!taxiDriverHireApprove[msg.sender]==true, 'This participant already approved for this driver.');

        taxiDriverHireApprove[msg.sender] = true;
        possibleDriver.approvalCount++;
	}
	
	function setDriver() external onlyManager() {
	    require(possibleDriver.approvalCount > participantsArray.length / 2, "Not enough votes for this driver");
        driver = possibleDriver;
        driver.approvalState = 1;
        delete possibleDriver;
	}
	
	function fireDriver() external onlyManager() {
	   	require(contractBalance >= driver.salary, "You cannot fire the driver because of insufficent contract amount");

	    driver.taxiDriverAccountBalance += driver.salary;
	    contractBalance -= driver.salary;
	    driver.approvalState = 0; // This allows driver to withdraw his money after he get fired.
	    //delete driver;
	}
	
	function payTaxiCharge () external payable{
	    require(msg.value>0, "Taxi charge cannot be zero");
	    contractBalance += msg.value;
	}
	
	function releaseSalary() external onlyManager() {
        require(driverLastPayDate + 30 days <= block.timestamp , "You can only call this function once in a month");
        require(contractBalance>=driver.salary,"Not enough money to provide drivers salary");
	    driver.taxiDriverAccountBalance += driver.salary;
	    contractBalance -= driver.salary;
	    driverLastPayDate = block.timestamp;
    }
	
	function getSalary() external onlyDriver() {
        require(driver.taxiDriverAddress.balance > 0, "No money in driver balance.");
        uint temporaryAmount = driver.taxiDriverAccountBalance;
        driver.taxiDriverAccountBalance = 0;
        
        
	    driver.taxiDriverAddress.transfer(temporaryAmount);
	}
	
	function payCarExpenses() external onlyManager(){
        require(block.timestamp >= lastCarExpenseTime + 180 days, "You can only call once this functions in 6 months");
        lastCarExpenseTime = block.timestamp;
        contractBalance -= fixExpenses;
        carDealer.transfer(fixExpenses);
	}
	
	function payDividend() external onlyManager(){
	    require(block.timestamp > 180 days + lastDividedTime, "You can only call once this functions in 6 months");
	    uint profitAmount = address(this).balance;
        lastDividedTime = block.timestamp;
        
        if(block.timestamp >= lastCarExpenseTime + 180 days){
            profitAmount -= fixExpenses;
        }
        if(driverLastPayDate + 30 days <= block.timestamp)
            profitAmount -= driver.salary;
        
        uint sharePerParticipant = profitAmount / participantsArray.length;
        
        for(uint8 i=0; i<participantsArray.length; i++){
            participantMap[participantsArray[i]].participantAccountBalance += sharePerParticipant;
        }
	}
	
	function getDividend() external onlyParticipant(){
        uint tempBalance = participantMap[msg.sender].participantAccountBalance;
        participantMap[msg.sender].participantAccountBalance = 0;
        contractBalance -= tempBalance;
        msg.sender.transfer(tempBalance);
	}
	
	fallback () external payable {
    
    }
	
    receive () external payable {
    
    }
    
    //------------------------------------------------External Functions End --------------------------------------------------------

}